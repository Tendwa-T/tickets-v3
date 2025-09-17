package com.tendwa.learning.ticketmanagement.event.dtos.ticketType;

import lombok.Data;

@Data
public class TicketTypeAvailabilityResponse {
    private Boolean available;
    private Integer remaining;
}
