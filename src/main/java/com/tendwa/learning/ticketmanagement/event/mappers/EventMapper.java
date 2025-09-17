package com.tendwa.learning.ticketmanagement.event.mappers;

import com.tendwa.learning.ticketmanagement.event.dtos.event.EventDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.EventRequestPrims;
import com.tendwa.learning.ticketmanagement.event.entities.Event;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    Event toEntity(EventDto eventDto);

    EventDto toDto(Event event);

    EventDto toDto(EventRequestPrims eventRequestPrims);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(EventDto eventDto, @MappingTarget Event event);
}