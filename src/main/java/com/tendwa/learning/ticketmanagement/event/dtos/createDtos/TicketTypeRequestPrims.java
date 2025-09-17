package com.tendwa.learning.ticketmanagement.event.dtos.createDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TicketTypeRequestPrims implements Serializable {
    private Long id;

    private Long eventId;

    @NotNull
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer quantityTotal;
    @NotNull
    private Integer quantitySold;
    @NotNull
    private Long createdBy;
    @NotNull
    private Instant createdAt;
}