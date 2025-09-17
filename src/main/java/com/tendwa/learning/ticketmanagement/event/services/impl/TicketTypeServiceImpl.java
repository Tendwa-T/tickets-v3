package com.tendwa.learning.ticketmanagement.event.services.impl;

import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.cart.entities.Cart;
import com.tendwa.learning.ticketmanagement.cart.repositories.CartItemRepository;
import com.tendwa.learning.ticketmanagement.cart.repositories.CartRepository;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.TicketTypeRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeAvailabilityResponse;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
import com.tendwa.learning.ticketmanagement.event.entities.Event;
import com.tendwa.learning.ticketmanagement.event.entities.Ticket;
import com.tendwa.learning.ticketmanagement.event.entities.TicketType;
import com.tendwa.learning.ticketmanagement.event.mappers.TicketMapper;
import com.tendwa.learning.ticketmanagement.event.mappers.TicketTypeMapper;
import com.tendwa.learning.ticketmanagement.event.repositories.EventRepository;
import com.tendwa.learning.ticketmanagement.event.repositories.TicketRepository;
import com.tendwa.learning.ticketmanagement.event.repositories.TicketTypeRepository;
import com.tendwa.learning.ticketmanagement.event.services.TicketTypeService;
import com.tendwa.learning.ticketmanagement.generic.exceptions.BadRequestException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.ResourceNotFoundException;
import com.tendwa.learning.ticketmanagement.generic.exceptions.TicketException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;
    private final AuthServiceImpl authServiceImpl;
    private final TicketTypeMapper ticketTypeMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketTypeDto createTicketType(TicketTypeRequestPrims requestPrims) {
        Event event = eventRepository.findById(requestPrims.getEventId()).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Event",
                        "EventID",
                        requestPrims.getEventId().toString()
                )
        );
        List<TicketType> ticketType = ticketTypeRepository.findAllByEvent_IdAndNameContainingIgnoreCase(requestPrims.getEventId(), requestPrims.getName());
        if(!ticketType.isEmpty()){
            throw new BadRequestException(String.format("Ticket Type %s already exists", requestPrims.getName()));
        }

        TicketType ticketTypeEntity = new TicketType();
        ticketTypeEntity.setName(requestPrims.getName());
        ticketTypeEntity.setEvent(event);
        ticketTypeEntity.setDescription(requestPrims.getDescription());
        ticketTypeEntity.setPrice(requestPrims.getPrice());
        ticketTypeEntity.setQuantityTotal(requestPrims.getQuantityTotal());
        ticketTypeEntity.setQuantitySold(0);
        ticketTypeEntity.setCreatedBy(authServiceImpl.getCurrentUser().getId());
        ticketTypeEntity.setCreatedAt(Instant.now());

        return ticketTypeMapper.toDto(ticketTypeRepository.save(ticketTypeEntity));
    }

    @Override
    public TicketTypeDto updateTicketType(Long ticketTypeID, TicketTypeRequestPrims requestPrims) {
        TicketType existingTicketType = ticketTypeRepository.findById(ticketTypeID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Ticket Type",
                        "TicketTypeID",
                        ticketTypeID.toString()
                )
        );

        TicketType ticketType = ticketTypeMapper.toEntity(requestPrims);
        return ticketTypeMapper.toDto(ticketTypeMapper.partialUpdate(ticketTypeMapper.toDto(ticketType), existingTicketType));
    }

    @Override
    public void deleteTicketType(Long ticketTypeID) {
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Ticket Type",
                        "TicketTypeID",
                        ticketTypeID.toString()
                )
        );
        ticketTypeRepository.delete(ticketType);
    }

    @Override
    public Page<TicketTypeDto> getTicketTypesByEvent(Long eventID, Pageable pageable) {
        return ticketTypeRepository.findAllByEvent_Id(eventID, pageable);
    }

    @Override
    public TicketTypeAvailabilityResponse checkAvailability(Long ticketTypeID, Long eventID, Integer quantity) {
        Event event = eventRepository.findById(eventID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Event",
                        "EventID",
                        eventID.toString()
                )
        );
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Ticket Type",
                        "TicketTypeID",
                        ticketTypeID.toString()
                )
        );

        TicketTypeAvailabilityResponse ticketTypeAvailabilityResponse = new TicketTypeAvailabilityResponse();

        if(ticketType.getRemainingTickets() > 0){
            ticketTypeAvailabilityResponse.setAvailable(true);
            ticketTypeAvailabilityResponse.setRemaining(ticketType.getRemainingTickets());
            return ticketTypeAvailabilityResponse;
        }

        ticketTypeAvailabilityResponse.setAvailable(false);
        ticketTypeAvailabilityResponse.setRemaining(0);

        return ticketTypeAvailabilityResponse;
    }

    @Override
    @Transactional
    public List<TicketDto> addToCart(Long ticketTypeID, Integer quantity) {
        User user = authServiceImpl.getCurrentUser();
        Cart cart = cartRepository.findByCustomer_Id(user.getId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(user);
            cartRepository.save(cart);
        }
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeID).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Ticket Type",
                        "TicketTypeID",
                        ticketTypeID.toString()
                )
        );

        if (ticketType.getRemainingTickets() < quantity) {
            throw new TicketException("Not enough tickets");
        }

        for (int i = 0; i < quantity; i++) {
            Ticket ticket = new Ticket();
            ticket.setTicketType(ticketType);
            ticket.setUser(user);
            ticketRepository.save(ticket);
            cart.addItem(ticket);
            ticketType.setQuantitySold(ticketType.getQuantitySold() + 1);
            ticketTypeRepository.save(ticketType);
        }

        cartRepository.save(cart);

        return cart.getCartItems().stream().map(
                cartItem -> ticketMapper.toDto(cartItem.getTicket())
        ).toList();
    }
}
