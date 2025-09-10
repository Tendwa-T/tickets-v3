package com.tendwa.learning.ticketmanagement.auth.mappers;

import com.tendwa.learning.ticketmanagement.auth.dtos.role.RoleDto;
import com.tendwa.learning.ticketmanagement.auth.entities.Role;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleDto roleDto, @MappingTarget Role role);
}