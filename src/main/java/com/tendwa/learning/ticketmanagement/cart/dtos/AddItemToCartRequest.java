package com.tendwa.learning.ticketmanagement.cart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull
    private Long ticketID;
}
