package com.tendwa.learning.ticketmanagement.auth.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(Long userId);
}