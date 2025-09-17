package com.tendwa.learning.ticketmanagement.auth.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String username);

    Optional<User> findByEmail(String email);
}