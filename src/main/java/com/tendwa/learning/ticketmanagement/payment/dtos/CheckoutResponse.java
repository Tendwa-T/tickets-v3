package com.tendwa.learning.ticketmanagement.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResponse {
    private Long orderID;
    private String checkoutUrl;
}
