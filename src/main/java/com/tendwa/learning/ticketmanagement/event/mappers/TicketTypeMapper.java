package com.tendwa.learning.ticketmanagement.event.mappers;

import com.tendwa.learning.ticketmanagement.event.entities.TicketType;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.TicketTypeRequestPrims;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {EventMapper.class})
public interface TicketTypeMapper {
    TicketType toEntity(TicketTypeDto ticketTypeDto);

    TicketTypeDto toDto(TicketType ticketType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TicketType partialUpdate(TicketTypeDto ticketTypeDto, @MappingTarget TicketType ticketType);

    @Mapping(source = "eventId", target = "event.id")
    TicketType toEntity(TicketTypeRequestPrims ticketTypeRequestPrims);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eventId", target = "event.id")
    TicketType partialUpdate(TicketTypeRequestPrims ticketTypeRequestPrims, @MappingTarget TicketType ticketType);
}