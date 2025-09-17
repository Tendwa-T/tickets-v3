package com.tendwa.learning.ticketmanagement.auth.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(Long userId);

    UserRole findByRole_Name(String roleName);

    Optional<UserRole> findByRole_Id(Long roleId);

    Optional<UserRole> findByUser_IdAndRole_Id(Long userId, Long roleId);
}