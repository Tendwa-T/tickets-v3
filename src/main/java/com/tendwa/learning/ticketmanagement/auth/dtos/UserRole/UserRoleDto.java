package com.tendwa.learning.ticketmanagement.auth.dtos.UserRole;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.entities.UserRole;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserRole}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleDto implements Serializable {
    Long id;
    Long roleId;
    String roleName;
}