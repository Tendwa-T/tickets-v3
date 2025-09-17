package com.tendwa.learning.ticketmanagement.payment.services.impl;

import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.cart.repositories.CartRepository;
import com.tendwa.learning.ticketmanagement.cart.services.CartService;
import com.tendwa.learning.ticketmanagement.event.repositories.TicketRepository;
import com.tendwa.learning.ticketmanagement.generic.exceptions.CartException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.PaymentException;
import com.tendwa.learning.ticketmanagement.order.entities.Order;
import com.tendwa.learning.ticketmanagement.order.repositories.OrderRepository;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutRequest;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutResponse;
import com.tendwa.learning.ticketmanagement.payment.dtos.WebhookRequest;
import com.tendwa.learning.ticketmanagement.payment.gateways.PaymentGateway;
import com.tendwa.learning.ticketmanagement.payment.services.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final AuthServiceImpl authServiceImpl;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    private final CartService cartService;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElseThrow(
                ()->new CartException("Cart not found")
        );
        if (cart.isEmpty()){
            throw new CartException("Cart is empty");
        }
        var order = Order.fromCart(cart, authServiceImpl.getCurrentUser());
        orderRepository.save(order);
        order.getOrderItems().forEach(item -> {
            var ticket = item.getTicket();
            ticket.setOrderId(order.getId());
            ticketRepository.save(ticket);
        });

        try{
            var session  = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException ex){
            orderRepository.delete(order);
            throw new PaymentException(ex.getMessage());
        }
    }

    @Override
    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
