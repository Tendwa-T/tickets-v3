package com.tendwa.learning.ticketmanagement.event.services;

import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.TicketTypeRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeAvailabilityResponse;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketTypeService {
    TicketTypeDto createTicketType(TicketTypeRequestPrims requestPrims);

    TicketTypeDto updateTicketType(Long ticketTypeID, TicketTypeRequestPrims requestPrims);

    void deleteTicketType(Long ticketTypeID);

    Page<TicketTypeDto> getTicketTypesByEvent(Long eventID, Pageable pageable);

    TicketTypeAvailabilityResponse checkAvailability(Long ticketTypeID, Long eventID, Integer quantity);

    List<TicketDto> addToCart(Long ticketTypeID, Integer quantity);
}
