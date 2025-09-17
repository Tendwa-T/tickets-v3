package com.tendwa.learning.ticketmanagement.event.dtos.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
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
public class TicketDto implements Serializable {
    private Long id;
    @NotNull
    private TicketTypeDto ticketType;
    @NotNull
    private UserDto user;
    private Long orderId;
    private String status;
    @NotNull
    private Instant issueAt;
}