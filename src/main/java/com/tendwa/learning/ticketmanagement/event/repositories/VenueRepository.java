package com.tendwa.learning.ticketmanagement.event.repositories;

import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import com.tendwa.learning.ticketmanagement.event.entities.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByNameEqualsIgnoreCaseAndAddressEqualsIgnoreCase(String name, String address);

    Page<Venue> findAllByCapacityIsGreaterThanEqual(Integer capacityIsGreaterThan, Pageable pageable);
}