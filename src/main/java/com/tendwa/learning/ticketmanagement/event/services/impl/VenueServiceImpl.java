package com.tendwa.learning.ticketmanagement.event.services.impl;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.VenueRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import com.tendwa.learning.ticketmanagement.event.mappers.EventMapper;
import com.tendwa.learning.ticketmanagement.event.mappers.VenueMapper;
import com.tendwa.learning.ticketmanagement.event.repositories.EventRepository;
import com.tendwa.learning.ticketmanagement.event.repositories.VenueRepository;
import com.tendwa.learning.ticketmanagement.event.services.VenueService;
import com.tendwa.learning.ticketmanagement.generic.enums.RoleEnum;
import com.tendwa.learning.ticketmanagement.generic.exceptions.BadRequestException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final AuthServiceImpl authServiceImpl;
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    @Override
    public VenueDto createVenue(VenueRequestPrims requestPrims) {
        User auth = validateCurrentUser();

        var venueExists = venueRepository.existsByNameEqualsIgnoreCaseAndAddressEqualsIgnoreCase(requestPrims.getName(), requestPrims.getAddress());
        if(venueExists){
           throw new BadRequestException("Venue already exists");
        }
        Venue venue = new Venue();
        venue.setName(requestPrims.getName());
        venue.setAddress(requestPrims.getAddress());
        venue.setCity(requestPrims.getCity());
        venue.setCountry(requestPrims.getCountry());
        venue.setCapacity(requestPrims.getCapacity());
        venue.setCreatedBy(auth);

        return venueMapper.toDto(venueRepository.save(venue));
    }

    @Override
    public VenueDto updateVenue(Long venueID, VenueRequestPrims requestPrims) {
        User auth = validateCurrentUser();

        // Check if the venue exists
        Venue venue = venueRepository.findById(venueID).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Venue",
                        "VenueID",
                        venueID.toString()
                )
        );

        VenueDto venueDto = venueMapper.toDto(requestPrims);
        return venueMapper.toDto(venueRepository.save(venueMapper.partialUpdate(venueDto,venue)));
    }

    @Override
    public void deleteVenue(Long venueID) {
        // Validate the user calling the action
        User auth = validateCurrentUser();

        // Find the Venue
        Venue venue = venueRepository.findById(venueID).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Venue",
                        "VenueID",
                        venueID.toString()
                )
        );

        // Check if there are upcoming events with this venue
        var upcomingEvents = eventRepository.findAllByVenueAndStartTimeAfter(venue, Instant.now());
        if (upcomingEvents.isEmpty()){
            venue.setIsDeleted(true);
            venueRepository.save(venue);
        }else {
            throw new BadRequestException("Venue with upcoming events cannot be deleted");
        }
    }

    @Override
    public boolean venueAvailability(Long venueID, Instant startDate, Instant endDate) {
        if(!venueRepository.existsById(venueID)){
            throw new ResourceNotFoundException(
                    "Venue",
                    "VenueID",
                    venueID.toString()
            );
        }
        boolean available = false;
        var event = eventRepository.findByVenue_IdAndStartTimeBetweenOrEndTimeBetween(venueID, startDate,endDate,startDate,endDate);
        if (event.isEmpty()){
            available = true;
        }
        return available;
    }

    @Override
    public Page<VenueDto> getVenues(Pageable pageable) {
        return venueRepository.findAll(pageable).map(venueMapper::toDto);
    }

    @Override
    public VenueDto getVenue(Long venueID) {
        return venueMapper.toDto(venueRepository.findById(venueID).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Venue",
                        "VenueID",
                        venueID.toString()
                )
        ));
    }

    @Override
    public Page<VenueDto> getByCapacity(Integer capacity, Pageable pageable) {
        return venueRepository.findAllByCapacityIsGreaterThanEqual(capacity, pageable).map(venueMapper::toDto);
    }

    User validateCurrentUser() {
        User auth = authServiceImpl.getCurrentUser();
        if(auth == null){
            throw new AccessDeniedException("Log in first to Create Venue");
        }

        boolean cur_allowed = auth.getUserRoles()
                .stream()
                .anyMatch(role ->
                        role.getRole().getName().equals(RoleEnum.ADMIN) ||
                                role.getRole().getName().equals(RoleEnum.ORGANIZER)

                );

        if (!cur_allowed) {
            throw new AccessDeniedException("You are not allowed to perform this action.");
        }

        return auth;
    }
}
