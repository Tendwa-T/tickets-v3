package com.tendwa.learning.ticketmanagement.event.dtos.createDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.event.entities.Ticket;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Ticket}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketRequestPrims implements Serializable {
    private Long id;
    private Long ticketTypeId;
    private Long userId;
    private Long orderId;
    private String status;
    @NotNull
    private Instant issueAt;
}