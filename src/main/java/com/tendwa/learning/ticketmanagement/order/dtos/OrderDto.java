package com.tendwa.learning.ticketmanagement.order.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.order.entities.Order;
import com.tendwa.learning.ticketmanagement.order.entities.OrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link Order}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {
    private Long id;
    @NotNull
    private UserDto customer;
    @NotNull
    private String paymentStatus;
    @NotNull
    private Instant createdAt;
    @NotNull
    private BigDecimal totalPrice;
    private Set<OrderItem> orderItems = new LinkedHashSet<>();
}