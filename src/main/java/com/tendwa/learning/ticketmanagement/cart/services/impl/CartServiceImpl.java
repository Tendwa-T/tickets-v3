package com.tendwa.learning.ticketmanagement.cart.services.impl;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.cart.dtos.CartDto;
import com.tendwa.learning.ticketmanagement.cart.dtos.CartItemDto;
import com.tendwa.learning.ticketmanagement.cart.entities.Cart;
import com.tendwa.learning.ticketmanagement.cart.mappers.CartItemMapper;
import com.tendwa.learning.ticketmanagement.cart.mappers.CartMapper;
import com.tendwa.learning.ticketmanagement.cart.repositories.CartRepository;
import com.tendwa.learning.ticketmanagement.cart.services.CartService;
import com.tendwa.learning.ticketmanagement.event.repositories.TicketRepository;
import com.tendwa.learning.ticketmanagement.generic.exceptions.CartException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final TicketRepository ticketRepository;
    private final CartItemMapper cartItemMapper;
    private final AuthServiceImpl authServiceImpl;

    @Override
    public CartDto createCart() {
        User customer = authServiceImpl.getCurrentUser();
        Cart cart = cartRepository.findByCustomer_Id(customer.getId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(authServiceImpl.getCurrentUser());
        }

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartItemDto addToCart(UUID cartID, Long ticketID) {
        var cart = cartRepository.getCartWithItems(cartID).orElseThrow(
                ()-> new CartException("Cart not found")
        );
        var ticket = ticketRepository.findById(ticketID).orElseThrow(
                ()-> new CartException("Ticket not found")
        );

        var cartItem = cart.addItem(ticket);
        cartRepository.save(cart);

        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartDto getCart(UUID cartID) {
        return cartMapper.toDto(cartRepository.getCartWithItems(cartID).orElseThrow(
                ()-> new CartException("Cart not found")
        ));
    }

    @Override
    public CartItemDto updateCartItem(UUID cartID, Long ticketID, Integer quantity) {
        var cart = cartRepository.getCartWithItems(cartID).orElseThrow(
                ()-> new CartException("Cart not found")
        );
        var cartItem = cart.getItem(ticketID);
        if (cartItem == null) {
            throw new CartException("Ticket not found");
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void removeCartItem(UUID cartID, Long ticketID) {
        var cart =  cartRepository.getCartWithItems(cartID).orElseThrow(
                ()-> new CartException("Cart not found")
        );
        cart.removeItem(ticketID);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(UUID cartID) {
        var cart = cartRepository.getCartWithItems(cartID).orElseThrow(
                ()-> new CartException("Cart not found")
        );
        cart.clearCart();
        cartRepository.save(cart);
    }
}
