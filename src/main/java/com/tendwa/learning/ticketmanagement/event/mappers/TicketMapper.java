package com.tendwa.learning.ticketmanagement.event.mappers;

import com.tendwa.learning.ticketmanagement.auth.mappers.UserMapper;
import com.tendwa.learning.ticketmanagement.event.entities.Ticket;
import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.TicketRequestPrims;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {TicketTypeMapper.class, UserMapper.class})
public interface TicketMapper {
    Ticket toEntity(TicketDto ticketDto);

    TicketDto toDto(Ticket ticket);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ticket partialUpdate(TicketDto ticketDto, @MappingTarget Ticket ticket);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "ticketTypeId", target = "ticketType.id")
    Ticket toEntity(TicketRequestPrims ticketRequestPrims);

    @InheritInverseConfiguration(name = "toEntity")
    TicketRequestPrims toDto1(Ticket ticket);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Ticket partialUpdate(TicketRequestPrims ticketRequestPrims, @MappingTarget Ticket ticket);
}