package com.tendwa.learning.ticketmanagement.event.dtos.venue;


import lombok.Data;

import java.time.Instant;

@Data
public class VenueAvailabilityParams {
    private Long venueID;
    private Instant startDate;
    private Instant endDate;
}
