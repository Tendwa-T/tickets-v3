package com.tendwa.learning.ticketmanagement.order.services;

import com.tendwa.learning.ticketmanagement.order.dtos.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAllOrders(Pageable pageable);

    OrderDto getOrderById(Long id);
}
