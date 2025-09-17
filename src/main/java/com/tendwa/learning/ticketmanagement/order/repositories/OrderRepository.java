package com.tendwa.learning.ticketmanagement.order.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.order.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems.ticket")
    @Query("SELECT o FROM Order o WHERE o.customer =:customer")
    Page<Order> getOrderByCustomer(@Param("customer") User customer, Pageable pageable);


    @EntityGraph(attributePaths = "prderItems.ticket")
    @Query("SELECT o FROM Order o where o.id =:orderID")
    Optional<Order> getOrderWithItems(@Param("orderID") Long orderID);
}