package com.tendwa.learning.ticketmanagement.payment.services;

import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutRequest;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutResponse;
import com.tendwa.learning.ticketmanagement.payment.dtos.WebhookRequest;

public interface CheckoutService {

    CheckoutResponse checkout(CheckoutRequest request);

    void handleWebhookEvent(WebhookRequest request);

}
