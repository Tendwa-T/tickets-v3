package com.tendwa.learning.ticketmanagement.payment.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "CartID is required")
    private UUID cartId;
}
