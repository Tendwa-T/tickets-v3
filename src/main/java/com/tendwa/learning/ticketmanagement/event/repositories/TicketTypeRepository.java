package com.tendwa.learning.ticketmanagement.event.repositories;

import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
import com.tendwa.learning.ticketmanagement.event.entities.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    List<TicketType> findAllByEvent_IdAndNameContainingIgnoreCase(Long eventId, String name);

    Page<TicketTypeDto> findAllByEvent_Id(Long eventId, Pageable pageable);
}