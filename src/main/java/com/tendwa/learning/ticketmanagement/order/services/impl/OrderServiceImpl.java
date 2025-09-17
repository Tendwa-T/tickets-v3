package com.tendwa.learning.ticketmanagement.order.services.impl;

import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.generic.exceptions.CartException;
import com.tendwa.learning.ticketmanagement.order.dtos.OrderDto;
import com.tendwa.learning.ticketmanagement.order.mappers.OrderMapper;
import com.tendwa.learning.ticketmanagement.order.repositories.OrderRepository;
import com.tendwa.learning.ticketmanagement.order.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final AuthServiceImpl authServiceImpl;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        var auth = authServiceImpl.getCurrentUser();
        var orders = orderRepository.getOrderByCustomer(auth, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        var order = orderRepository.getOrderWithItems(id).orElseThrow(
                ()-> new CartException("Order not found")
        );
        var user = authServiceImpl.getCurrentUser();

        if(!order.isPlacedBy(user)){
            throw new AccessDeniedException("You are not allowed to access this order");
        }
        return orderMapper.toDto(order);
    }
}
