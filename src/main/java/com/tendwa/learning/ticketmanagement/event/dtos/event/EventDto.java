package com.tendwa.learning.ticketmanagement.event.dtos.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import com.tendwa.learning.ticketmanagement.event.entities.Event;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Event}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto implements Serializable {
    private Long id;

    private UserDto organizer;

    @Size(max = 255)
    private String title;

    private String description;

    private Instant startTime;

    private Instant endTime;

    private Integer capacity;

    private String status;

    private UserDto createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    private Boolean isDeleted = false;

    private VenueDto venue;
}