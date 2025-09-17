package com.tendwa.learning.ticketmanagement.event.repositories;

import com.tendwa.learning.ticketmanagement.event.entities.Event;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndOrganizer_Id(Long id, Long organizerId);


    Page<Event> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);

    Page<Event> findByOrganizer_Id(Long organizerId, Pageable pageable);


    List<Event> findAllByVenueAndStartTimeAfter(Venue venue, Instant startTimeAfter);

    List<Event> findByVenue_IdAndStartTimeBetweenOrEndTimeBetween(Long venueId, Instant startTimeAfter, Instant startTimeBefore, Instant endTimeAfter, Instant endTimeBefore);
}