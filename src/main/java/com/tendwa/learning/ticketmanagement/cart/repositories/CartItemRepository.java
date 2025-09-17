package com.tendwa.learning.ticketmanagement.cart.repositories;

import com.tendwa.learning.ticketmanagement.cart.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCart_IdAndTicket_TicketType_Id(UUID cartId, Long ticketTicketTypeId);
}