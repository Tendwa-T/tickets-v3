package com.tendwa.learning.ticketmanagement.event.dtos.venue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Venue}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueDto implements Serializable {
    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String country;

    private Integer capacity;

    private Boolean isDeleted = false;

    private Instant createdAt;
}