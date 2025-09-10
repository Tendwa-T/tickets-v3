package com.tendwa.learning.ticketmanagement.auth.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "First Name is Required")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    private String phonNumber;

    @NotBlank(message = "Password is Required")
    @Size(min = 6, max = 25, message = "Password must be between 6 to 25 characters")
    private String password;
}
