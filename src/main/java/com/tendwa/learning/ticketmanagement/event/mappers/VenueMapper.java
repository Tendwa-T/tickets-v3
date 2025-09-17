package com.tendwa.learning.ticketmanagement.event.mappers;

import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.VenueRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface VenueMapper {
    Venue toEntity(VenueDto venueDto);

    VenueDto toDto(Venue venue);

    VenueDto toDto(VenueRequestPrims venueRequestPrims);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Venue partialUpdate(VenueDto venueDto, @MappingTarget Venue venue);
}