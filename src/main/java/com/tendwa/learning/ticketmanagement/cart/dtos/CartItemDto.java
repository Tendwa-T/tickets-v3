package com.tendwa.learning.ticketmanagement.cart.dtos;

import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private TicketDto ticket;
    private Integer quantity;
    private BigDecimal totalPrice;
}
