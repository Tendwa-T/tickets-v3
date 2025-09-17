package com.tendwa.learning.ticketmanagement.auth.services;

import com.tendwa.learning.ticketmanagement.auth.dtos.user.CreateUserRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UpdateUserRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    // User CRUD
    UserDto registerUser(CreateUserRequest userRequest);
    List<UserDto> getUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUser(Long id, UpdateUserRequest userDto);

    // Password Modification
    Map<String, Object> requestPasswordReset(String email);
    Map<String, Object> resetPassword(String resetToken, String newPassword);

    // Activate / Deactivate User
    boolean activateUser(Long userId);
    boolean deactivateUser(Long userId);

    // Role and Access Management
    boolean assignRole(Long userId, Long roleId);

}
