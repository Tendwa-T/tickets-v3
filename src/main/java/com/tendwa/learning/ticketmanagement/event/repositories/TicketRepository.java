package com.tendwa.learning.ticketmanagement.event.repositories;

import com.tendwa.learning.ticketmanagement.event.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}