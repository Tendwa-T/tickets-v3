package com.tendwa.learning.ticketmanagement.payment.controllers;

import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutRequest;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutResponse;
import com.tendwa.learning.ticketmanagement.payment.dtos.WebhookRequest;
import com.tendwa.learning.ticketmanagement.payment.services.CheckoutService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/checkout")
@Tag(name = "Checkout")
@SecurityRequirement(name="bearerAuth")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(
            @RequestBody CheckoutRequest checkoutRequest
    ){
        return ResponseEntity.ok(
                ApiResponse.<CheckoutResponse>builder()
                        .message("Checking out...")
                        .data(checkoutService.checkout(checkoutRequest))
                        .build()
        );
    }

    @PostMapping("/webhook")
    public void webhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
            ){
        checkoutService.handleWebhookEvent(new WebhookRequest(headers,payload));
    }
}
