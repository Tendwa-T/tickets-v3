package com.tendwa.learning.ticketmanagement.auth.dtos.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.tendwa.learning.ticketmanagement.auth.entities.Role}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto implements Serializable {
    Long id;
    @NotNull
    String name;
}