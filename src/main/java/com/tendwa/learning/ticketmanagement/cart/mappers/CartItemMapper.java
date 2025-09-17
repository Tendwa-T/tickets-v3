package com.tendwa.learning.ticketmanagement.cart.mappers;

import com.tendwa.learning.ticketmanagement.cart.dtos.CartItemDto;
import com.tendwa.learning.ticketmanagement.cart.entities.CartItem;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {
    CartItem toEntity(CartItemDto cartItemDto);

    @Mapping(target = "totalPrice" , expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartItem partialUpdate(CartItemDto cartItemDto, @MappingTarget CartItem cartItem);
}