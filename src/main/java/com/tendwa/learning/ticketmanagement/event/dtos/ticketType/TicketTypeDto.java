package com.tendwa.learning.ticketmanagement.event.dtos.ticketType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.event.dtos.event.EventDto;
import com.tendwa.learning.ticketmanagement.event.entities.TicketType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link TicketType}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketTypeDto implements Serializable {
    private Long id;

    private EventDto event;

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    private BigDecimal price;

    private Integer quantityTotal;

    private Integer quantitySold;

    private Long createdBy;

    private Instant createdAt;
}