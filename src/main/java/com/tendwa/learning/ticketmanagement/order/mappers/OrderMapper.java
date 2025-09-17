package com.tendwa.learning.ticketmanagement.order.mappers;

import com.tendwa.learning.ticketmanagement.auth.mappers.UserMapper;
import com.tendwa.learning.ticketmanagement.order.entities.Order;
import com.tendwa.learning.ticketmanagement.order.dtos.OrderDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    Order toEntity(OrderDto orderDto);

    @AfterMapping
    default void linkOrderItems(@MappingTarget Order order) {
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }

    OrderDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDto orderDto, @MappingTarget Order order);
}