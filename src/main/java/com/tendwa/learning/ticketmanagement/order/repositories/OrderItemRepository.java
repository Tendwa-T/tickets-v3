package com.tendwa.learning.ticketmanagement.order.repositories;

import com.tendwa.learning.ticketmanagement.order.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}