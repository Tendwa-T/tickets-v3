package com.tendwa.learning.ticketmanagement.cart.repositories;

import com.tendwa.learning.ticketmanagement.cart.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, String> {

    @EntityGraph(attributePaths = "cartItems.ticket")
    @Query("SELECT c FROM Cart c WHERE c.id = :cartID")
    Optional<Cart> getCartWithItems(@Param("cartID") UUID cartID);

    Optional<Cart> findByCustomer_Id(Long customerId);
}