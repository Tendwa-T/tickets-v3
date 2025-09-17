package com.tendwa.learning.ticketmanagement.event.services;

import com.tendwa.learning.ticketmanagement.event.dtos.event.EventDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.EventRequestPrims;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventDto createEvent (EventRequestPrims request);
    EventDto updateEvent (EventRequestPrims request);
    EventDto publishEvent (Long organizerID, Long eventID);
    EventDto cancelEvent (Long organizerID, Long eventID);
    EventDto completeEvent (Long eventID);

    EventDto getEvent (Long organizerID, Long eventID);
    Page<EventDto> getEvents(Pageable pageable);
    Page<EventDto> getEvents(String keyword, Pageable pageable);
    Page<EventDto> getEvents(Long organizerID, Pageable pageable);
}
