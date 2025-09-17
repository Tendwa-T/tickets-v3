package com.tendwa.learning.ticketmanagement.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutSession {
    private String checkoutUrl;
}
