package com.tendwa.learning.ticketmanagement.payment.gateways;

import com.tendwa.learning.ticketmanagement.order.entities.Order;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutSession;
import com.tendwa.learning.ticketmanagement.payment.dtos.PaymentResult;
import com.tendwa.learning.ticketmanagement.payment.dtos.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);

    Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest);

}
