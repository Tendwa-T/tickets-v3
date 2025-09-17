package com.tendwa.learning.ticketmanagement.auth.services.impl;

import com.tendwa.learning.ticketmanagement.auth.dtos.user.CreateUserRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UpdateUserRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.auth.entities.PasswordReset;
import com.tendwa.learning.ticketmanagement.auth.entities.Role;
import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.entities.UserRole;
import com.tendwa.learning.ticketmanagement.auth.mappers.UserMapper;
import com.tendwa.learning.ticketmanagement.auth.repositories.PasswordResetRepository;
import com.tendwa.learning.ticketmanagement.auth.repositories.RoleRepository;
import com.tendwa.learning.ticketmanagement.auth.repositories.UserRepository;
import com.tendwa.learning.ticketmanagement.auth.repositories.UserRoleRepository;
import com.tendwa.learning.ticketmanagement.auth.services.UserService;
import com.tendwa.learning.ticketmanagement.generic.exceptions.DuplicateUserException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final PasswordResetRepository passwordResetRepository;
    private final AuthServiceImpl authServiceImpl;

    @Override
    @Transactional
    public UserDto registerUser(CreateUserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateUserException(userRequest.getEmail());
        }

        Role role = roleRepository.findByName("ATTENDEE");

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhoneNumber(userRequest.getPhonNumber());

        var savedUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "UserID",
                        id.toString()
                )
        );

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "Email",
                        email
                )
        );

        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserRequest userDto) {
        var user = userRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "UserID",
                        id.toString()
                )
        );
        return userMapper.toDto(userMapper.partialUpdate(userDto, user));
    }

    @Override
    @Transactional
    public Map<String, Object> requestPasswordReset(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "Email",
                        email
                )
        );
        var existing_pass_reset_token = passwordResetRepository.findAllByUser_IdAndUsed(user.getId(),false);

        existing_pass_reset_token.forEach(token-> token.setUsed(true));
        passwordResetRepository.saveAll(existing_pass_reset_token);

        var redirectToken = jwtServiceImpl.generateRedirectToken(user);

        var pass_reset = new PasswordReset();
        pass_reset.setUser(user);
        pass_reset.setToken(redirectToken.toString());

        var saved_pass_reset = passwordResetRepository.save(pass_reset);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Saved password reset token");
        responseBody.put("token", saved_pass_reset.getToken());

        return responseBody;
    }

    @Override
    public Map<String, Object> resetPassword(String resetToken, String newPassword) {
        var resetRecord = passwordResetRepository.findByTokenAndExpiresAtIsAfter(resetToken, Instant.now()).orElseThrow(
                ()->new ResourceNotFoundException(
                        "Token Record",
                        "Token",
                        "REDACTED"
                )
        );

        if (resetRecord.getUsed()){
            throw new AccessDeniedException("Access Denied! Reset Token has already been used");
        }

        var user =  resetRecord.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetRecord.setExpiresAt(Instant.now());
        resetRecord.setUsed(true);

        passwordResetRepository.save(resetRecord);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Password Reset Successfully");

        return responseBody;
    }

    @Override
    public boolean activateUser(Long userId) {
        var  user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "UserID",
                        userId.toString()
                )
        );
        user.setIsActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deactivateUser(Long userId) {
        var  user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "UserID",
                        userId.toString()
                )
        );
        user.setIsActive(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean assignRole(Long userId, Long roleId) {
        var auth = authServiceImpl.getCurrentUser();
        userRoleRepository.findByUser_IdAndRole_Id(auth.getId(), 1L).orElseThrow(
                ()->new AccessDeniedException("You are not authorized to perform this action")
        );

        var user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException(
                        "User",
                        "UserID",
                        userId.toString()
                )
        );

        var role = roleRepository.findById(roleId).orElseThrow(
                ()->new ResourceNotFoundException(
                        "Role",
                        "roleID",
                        roleId.toString()
                )
        );
        var userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        return true;
    }
}
