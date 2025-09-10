package com.tendwa.learning.ticketmanagement.auth.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tendwa.learning.ticketmanagement.auth.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    Long id;
    @NotNull
    @Size(max = 255)
    String email;
    @NotNull
    @Size(max = 255)
    String firstName;
    @NotNull
    @Size(max = 255)
    String lastName;
    @NotNull
    @Size(max = 50)
    String phoneNumber;
}