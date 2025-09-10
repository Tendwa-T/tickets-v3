package com.tendwa.learning.ticketmanagement.auth.mappers;

import com.tendwa.learning.ticketmanagement.auth.entities.UserRole;
import com.tendwa.learning.ticketmanagement.auth.dtos.UserRole.UserRoleDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper {
    @Mapping(source = "roleName", target = "role.name")
    @Mapping(source = "roleId", target = "role.id")
    UserRole toEntity(UserRoleDto userRoleDto);

    @InheritInverseConfiguration(name = "toEntity")
    UserRoleDto toDto(UserRole userRole);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserRole partialUpdate(@MappingTarget UserRole userRole, UserRoleDto userRoleDto);
}