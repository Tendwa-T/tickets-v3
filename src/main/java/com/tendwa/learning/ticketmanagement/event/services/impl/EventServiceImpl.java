package com.tendwa.learning.ticketmanagement.event.services.impl;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.mappers.UserMapper;
import com.tendwa.learning.ticketmanagement.auth.repositories.UserRepository;
import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.event.dtos.event.EventAuthValidation;
import com.tendwa.learning.ticketmanagement.event.dtos.event.EventDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.EventRequestPrims;
import com.tendwa.learning.ticketmanagement.event.entities.Event;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import com.tendwa.learning.ticketmanagement.event.mappers.EventMapper;
import com.tendwa.learning.ticketmanagement.event.repositories.EventRepository;
import com.tendwa.learning.ticketmanagement.event.repositories.VenueRepository;
import com.tendwa.learning.ticketmanagement.event.services.EventService;
import com.tendwa.learning.ticketmanagement.generic.enums.EventStatusEnum;
import com.tendwa.learning.ticketmanagement.generic.enums.RoleEnum;
import com.tendwa.learning.ticketmanagement.generic.exceptions.BadRequestException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;
    private final AuthServiceImpl authServiceImpl;
    private final UserMapper userMapper;
    private final EventRepository eventRepository;

    @Override
    public EventDto createEvent(EventRequestPrims request) {
        var auth = authServiceImpl.getCurrentUser();
        if(auth == null) {
            throw new AccessDeniedException("Kindly log in to continue");
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
        // ? Check the Organizer's roles / Permissions

            // 1. Fetch Organizer
        User organizer = userRepository.findById(request.getOrganizerId()).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "User",
                        "UserID",
                        request.getOrganizerId().toString()
                )
        );

            // 2. Check if they are an admin or an organizer
        boolean org_allowed = organizer.getUserRoles().stream()
                .anyMatch(role ->
                        role.getRole().getName().equals(RoleEnum.ADMIN) ||
                        role.getRole().getName().equals(RoleEnum.ORGANIZER)
                );

            // 3. If not deny access
        if(!org_allowed){
            throw new AccessDeniedException("Set organizer is not allowed to create an Event");
        }

        // Check Venue
        Venue venue = venueRepository.findById(request.getVenueId()).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Venue",
                        "VenueID",
                        request.getVenueId().toString()
                )
        );

        // Check Venue Capacity vs Quoted Event Capacity
        if (request.getCapacity() > venue.getCapacity()) {
            throw new BadRequestException("Capacity exceeded");
        }

        if (Instant.parse(request.getStartTime()).isAfter(Instant.parse(request.getEndTime()))) {
            throw new BadRequestException("Start time cannot be after end time");
        }

        if (Instant.parse(request.getEndTime()).isBefore(Instant.parse(request.getStartTime()))) {
            throw new BadRequestException("End time cannot be before Start time");
        }

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(venue);
        event.setStartTime(Instant.parse(request.getStartTime()));
        event.setEndTime(Instant.parse(request.getEndTime()));
        event.setOrganizer(organizer);
        event.setCapacity(request.getCapacity());
        event.setCreatedBy(auth);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto updateEvent(EventRequestPrims request) {
        var validation = validateEventAuth(request.getId());
        Event event = validateEventOnUpdate(request, validation);

        if (Instant.parse(request.getStartTime()).isBefore(Instant.now()) ) {
            throw new BadRequestException("Start time cannot be before now.");
        }
        if (Instant.parse(request.getStartTime()).isAfter(Instant.parse(request.getEndTime())) ) {
            throw new BadRequestException("Start time cannot be after end time.");
        }

        EventDto eventDto = eventMapper.toDto(request);

        return eventMapper.toDto(eventRepository.save(eventMapper.partialUpdate(eventDto, event)));
    }



    @Override
    public EventDto publishEvent(Long organizerID, Long eventID) {
        var validation = validateEventAuth(eventID);
        User auth = validation.getAuth();
        Event event = validation.getEvent();

        if(!Objects.equals(auth.getId(), event.getOrganizer().getId())){
            throw new AccessDeniedException("Only the Event Organizer can publish this event.");
        }

        if(event.getStatus()!= EventStatusEnum.DRAFT){
            throw new BadRequestException("Only Draft Events can be published.");
        }

        if(Instant.now().isAfter(event.getStartTime())){
            throw new BadRequestException("Cannot publish an event after start time.");
        }

        event.setStatus(EventStatusEnum.PUBLISHED);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto cancelEvent(Long organizerID, Long eventID) {
        // * Check if the current user is authorized to cancel the Event
       EventAuthValidation validation = validateEventAuth(eventID);
       Event event = validation.getEvent();
       User auth =  validation.getAuth();

        if(!Objects.equals(auth.getId(), event.getOrganizer().getId())){
            throw new AccessDeniedException("Only the Event Organizer can cancel this event.");
        }

        if(event.getStatus()== EventStatusEnum.CANCELLED){
            throw new BadRequestException("This event was already cancelled.");
        }

        // * If it was already published, initiate refunds
//        if(event.getStatus()== EventStatusEnum.PUBLISHED){
//
//        }

        if(
                event.getStatus()== EventStatusEnum.COMPLETED ||
                event.getStatus()== EventStatusEnum.EXPIRED
        ){
            throw new BadRequestException("Only Draft and Published Events can be cancelled.");
        }

        event.setStatus(EventStatusEnum.CANCELLED);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto completeEvent(Long eventID) {
        EventAuthValidation validation = validateEventAuth(eventID);
        Event event = validation.getEvent();

        if (event.getStatus() != EventStatusEnum.PUBLISHED ) {
            throw new BadRequestException("Only Published Events can be completed.");
        }

        if (Instant.now().isBefore(event.getEndTime())) {
            throw new BadRequestException("Event Cannot be completed before end time");
        }
        event.setStatus(EventStatusEnum.COMPLETED);
        return eventMapper.toDto(eventRepository.save(event));

    }

    @Override
    public EventDto getEvent(Long organizerID, Long eventID) {
        Event event;

        if (organizerID != null) {
            event = eventRepository.findByIdAndOrganizer_Id(eventID, organizerID)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Event",
                            String.format("EventID: %d and OrganizerID: %d", eventID, organizerID),
                            eventID.toString()
                    ));
        } else {
            event = eventRepository.findById(eventID)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Event",
                            "EventID",
                            eventID.toString()
                    ));
        }

        return eventMapper.toDto(event);
    }

    @Override
    public Page<EventDto> getEvents(Pageable pageable) {

        return eventRepository.findAll(pageable).map(eventMapper::toDto);
    }

    @Override
    public Page<EventDto> getEvents(String keyword, Pageable pageable) {
        return eventRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
                .map(eventMapper::toDto);
    }


    @Override
    public Page<EventDto> getEvents(Long organizerID, Pageable pageable) {
        boolean userExists = userRepository.existsById(organizerID);
        if(!userExists){
            throw new ResourceNotFoundException(
                    "User",
                    "UserID",
                    organizerID.toString()
            );
        }
        return eventRepository.findByOrganizer_Id(organizerID, pageable)
                .map(eventMapper::toDto);
    }

    private EventAuthValidation validateEventAuth(Long eventID){
        User auth = authServiceImpl.getCurrentUser();
        if (auth == null) {
            throw new AccessDeniedException("You need to login first.");
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

        Event event = eventRepository.findById(eventID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Event",
                        "EventID",
                        eventID.toString()
                )
        );

        return new EventAuthValidation(event, auth);
    }

    private static Event validateEventOnUpdate(EventRequestPrims request, EventAuthValidation validation) {
        User auth = validation.getAuth();
        Event event = validation.getEvent();

        if (!Objects.equals(event.getOrganizer().getId(), auth.getId())){
            throw new AccessDeniedException("Only the Event Organizer can update this event.");
        }

        if(event.getStatus()!= EventStatusEnum.DRAFT){
            throw new BadRequestException("Only Draft Events can be updated.");
        }

        if (request.getCapacity() > event.getCapacity()) {
            throw new BadRequestException("Capacity exceeded");
        }
        return event;
    }
}
