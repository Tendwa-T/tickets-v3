package com.tendwa.learning.ticketmanagement.auth.services;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String username);

    Optional<User> findByEmail(String username);
}