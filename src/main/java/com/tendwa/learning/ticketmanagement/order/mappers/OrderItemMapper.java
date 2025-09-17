package com.tendwa.learning.ticketmanagement.order.mappers;

import com.tendwa.learning.ticketmanagement.event.mappers.TicketMapper;
import com.tendwa.learning.ticketmanagement.order.dtos.OrderItemDto;
import com.tendwa.learning.ticketmanagement.order.entities.OrderItem;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderMapper.class, TicketMapper.class})
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDto orderItemDto);

    OrderItemDto toDto(OrderItem orderItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderItem partialUpdate(OrderItemDto orderItemDto, @MappingTarget OrderItem orderItem);
}