package com.tendwa.learning.ticketmanagement.auth.mappers;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}