package com.tendwa.learning.ticketmanagement.event.services;

import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.VenueRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface VenueService {
    // Create
    VenueDto createVenue(VenueRequestPrims requestPrims);

    // Update
    VenueDto updateVenue(Long venueID, VenueRequestPrims requestPrims);

    // Delete
    void deleteVenue(Long venueID);

    //Check Availability (startTime, EndTime)
    boolean venueAvailability(Long venueID, Instant startDate, Instant endDate);

    // Get Functions
    // Get All
    Page<VenueDto> getVenues(Pageable pageable);

    // Get by ID
    VenueDto getVenue(Long venueID);

    // Get by Capacity
    Page<VenueDto> getByCapacity(Integer capacity, Pageable pageable);
}
