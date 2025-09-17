package com.tendwa.learning.ticketmanagement.order.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.tendwa.learning.ticketmanagement.order.entities.OrderItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDto implements Serializable {
    private Long id;
    @NotNull
    private OrderDto order;
    @NotNull
    private TicketDto ticket;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal totalPrice;
}