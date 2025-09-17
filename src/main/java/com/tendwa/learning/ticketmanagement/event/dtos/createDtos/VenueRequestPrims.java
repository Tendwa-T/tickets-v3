package com.tendwa.learning.ticketmanagement.event.dtos.createDtos;

import lombok.Data;

import java.time.Instant;

@Data
public class VenueRequestPrims {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Integer capacity;
    private boolean isDeleted = false;
    private Long createdBy;
    private Instant createdAt;
}
