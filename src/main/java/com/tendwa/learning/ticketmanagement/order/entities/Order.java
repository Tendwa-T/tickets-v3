package com.tendwa.learning.ticketmanagement.order.entities;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.cart.entities.Cart;
import com.tendwa.learning.ticketmanagement.generic.enums.PaymentStatusEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private User customer;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnums status;


    @NotNull
    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();


    public static Order fromCart(Cart cart, User customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(PaymentStatusEnums.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        cart.getCartItems().forEach(item -> {
            OrderItem orderItem = new OrderItem(order, item.getTicket(), item.getQuantity());
            order.orderItems.add(orderItem);
        });
        return order;
    }

    public boolean isPlacedBy(User customer){
        return this.customer.equals(customer);
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = Instant.now();
    }
}