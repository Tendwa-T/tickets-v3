package com.tendwa.learning.ticketmanagement.event.dtos.ticketType;

import lombok.Data;

@Data
public class TicketTypeAvailabilityParams {
    private Long ticketTypeID;
    private Long eventID;
    private Integer quantity;
}
