package com.tendwa.learning.ticketmanagement.cart.entities;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.event.entities.Ticket;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    public BigDecimal getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(Long ticketID){
        return cartItems.stream()
                .filter(cartItem -> cartItem.getTicket().getId().equals(ticketID))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Ticket ticket) {
        var cartItem = getItem(ticket.getId());
        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }else{
            cartItem = new CartItem();
            cartItem.setTicket(ticket);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }
        return cartItem;
    }

    public void removeItem(Long ticketID) {
        var cartItem = getItem(ticketID);
        if(cartItem != null){
            if(cartItem.getQuantity()>1){
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            }else{
                cartItems.remove(cartItem);
                cartItem.setCart(null);
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty(){
        return cartItems.isEmpty();
    }

    @PrePersist
    public void prePersist(){
        dateCreated = LocalDate.now();
    }

}