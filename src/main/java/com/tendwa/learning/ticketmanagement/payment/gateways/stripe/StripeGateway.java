package com.tendwa.learning.ticketmanagement.payment.gateways.stripe;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.tendwa.learning.ticketmanagement.generic.enums.PaymentStatusEnums;
import com.tendwa.learning.ticketmanagement.generic.exceptions.PaymentException;
import com.tendwa.learning.ticketmanagement.order.entities.Order;
import com.tendwa.learning.ticketmanagement.order.entities.OrderItem;
import com.tendwa.learning.ticketmanagement.payment.dtos.CheckoutSession;
import com.tendwa.learning.ticketmanagement.payment.dtos.PaymentResult;
import com.tendwa.learning.ticketmanagement.payment.dtos.WebhookRequest;
import com.tendwa.learning.ticketmanagement.payment.gateways.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripeGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {

        try{
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl+"/checkout-success?orderId="+order.getId())
                    .setCancelUrl(websiteUrl+"/checkout-cancel?orderId="+order.getId())
                    .setPaymentIntentData(createPaymentIntent(order));

            order.getOrderItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex ){
            throw new PaymentException(ex.getMessage());
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {

        try{
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()){
                case "payment_intent.succeeded"->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatusEnums.PAID));

                case "payment_intent.cancelled"->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatusEnums.CANCELLED));

                case "payment_intent.failed"->
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatusEnums.FAILED));
                default ->
                    Optional.empty();
            };
        } catch (SignatureVerificationException ex){
            throw new PaymentException(ex.getMessage());
        }

    }


    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", order.getId().toString())
                .build();
    }

    private Long extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                ()-> new PaymentException("ExtractOrderID: Order id not found")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item){
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item){
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("kes")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item){
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getTicket().getTicketType().getName())
                .setDescription(String.format("This is a %s ticket", item.getTicket().getTicketType().getName()))
                .build();
    }


}

























