package com.tendwa.learning.ticketmanagement.event.dtos.createDtos;

import lombok.Data;

@Data
public class AddTicketsRequest {
    private Long ticketTypeID;
    private Integer quantity;
}
