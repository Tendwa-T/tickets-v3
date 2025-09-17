package com.tendwa.learning.ticketmanagement.event.dtos.createDtos;

import lombok.Data;

@Data
public class EventRequestPrims {
    private Long id;
    private Long organizerId;
    private String title;
    private String description;
    private Long venueId;
    private String startTime;
    private String endTime;
    private Integer capacity;
}
