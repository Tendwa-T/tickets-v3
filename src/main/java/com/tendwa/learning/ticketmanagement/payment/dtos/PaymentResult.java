package com.tendwa.learning.ticketmanagement.payment.dtos;

import com.tendwa.learning.ticketmanagement.generic.enums.PaymentStatusEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResult {
    private Long orderId;
    private PaymentStatusEnums paymentStatus;
}
