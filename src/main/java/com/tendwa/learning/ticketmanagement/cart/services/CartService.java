package com.tendwa.learning.ticketmanagement.cart.services;

import com.tendwa.learning.ticketmanagement.cart.dtos.CartDto;
import com.tendwa.learning.ticketmanagement.cart.dtos.CartItemDto;

import java.util.UUID;

public interface CartService {
    CartDto createCart();

    CartItemDto addToCart(UUID cartID, Long ticketID);

    CartDto getCart(UUID cartID);

    CartItemDto updateCartItem(UUID cartID, Long ticketID, Integer quantity);

    void removeCartItem(UUID cartID, Long ticketID);

    void clearCart(UUID cartID);
}
