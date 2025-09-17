package com.tendwa.learning.ticketmanagement.auth.repositories;

import com.tendwa.learning.ticketmanagement.auth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}