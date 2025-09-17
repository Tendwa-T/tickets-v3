package com.tendwa.learning.ticketmanagement.auth.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByTokenAndExpiresAtIsBefore(String token, Instant expiresAtBefore);


    List<PasswordReset> findAllByUser_IdAndUsed(Long userId, Boolean used);

    Optional<PasswordReset>  findByTokenAndExpiresAtIsAfter(String token, Instant expiresAtAfter);
}