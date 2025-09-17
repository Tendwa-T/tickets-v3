package com.tendwa.learning.ticketmanagement.event.controllers.event;

import com.tendwa.learning.ticketmanagement.event.dtos.event.EventDto;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.EventRequestPrims;
import com.tendwa.learning.ticketmanagement.event.services.EventService;
import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
import com.tendwa.learning.ticketmanagement.generic.response.PagedResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Event Controllers", description = "APIs to run Event logic")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/event")
@SecurityRequirement(name = "bearerAuth")
public class EventController {
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EventDto>> createEvent (
            @Valid @RequestBody EventRequestPrims req
    ) {
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event created successfully")
                        .data(eventService.createEvent(req))
                        .build()
        );
    }

    @GetMapping("/id")
    public ResponseEntity<ApiResponse<EventDto>> getEvent (
            @RequestParam(name = "orgID", required = false) Long orgID,
            @RequestParam(name = "eventID")  Long eventID
    ){
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event fetched successfully")
                        .data(eventService.getEvent(orgID, eventID))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<EventDto>>> getEvents(
            @ParameterObject @PageableDefault(size = 50, sort = "createdAt", page = 1) Pageable pageable
    ){
        Page<EventDto> events = eventService.getEvents(pageable);
        PagedResponse<EventDto> response = PagedResponse.<EventDto>builder()
                .content(events.getContent())
                .currentPage(events.getNumber() + 1)
                .totalPages(events.getTotalPages())
                .totalElements(events.getTotalElements())
                .size(events.getSize())
                .build();
        return ResponseEntity.ok(
                ApiResponse.<PagedResponse<EventDto>>builder()
                        .message("Event fetched successfully")
                        .data(response)
                        .build()
        );
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<EventDto>>> getEventsKeyword(
            @RequestParam String keyword,
            @ParameterObject @PageableDefault(size = 50, sort = "createdAt", page = 1) Pageable pageable
            ){
        Page<EventDto> events = eventService.getEvents(keyword, pageable);
        PagedResponse<EventDto> response = PagedResponse.<EventDto>builder()
                .content(events.getContent())
                .currentPage(events.getNumber() + 1)
                .totalPages(events.getTotalPages())
                .totalElements(events.getTotalElements())
                .size(events.getSize())
                .build();
        return ResponseEntity.ok(
                ApiResponse.<PagedResponse<EventDto>>builder()
                        .message("Event fetched successfully")
                        .data(response)
                        .build()
        );
    }


    @GetMapping("/search/organizer")
    public ResponseEntity<ApiResponse<PagedResponse<EventDto>>> getEventsOrganizer(
            @RequestParam Long organizerID,
            @ParameterObject @PageableDefault(size = 50, sort = "createdAt", page = 1) Pageable pageable
    ){
        Page<EventDto> events = eventService.getEvents(organizerID, pageable);
        PagedResponse<EventDto> response = PagedResponse.<EventDto>builder()
                .content(events.getContent())
                .currentPage(events.getNumber() + 1)
                .totalPages(events.getTotalPages())
                .totalElements(events.getTotalElements())
                .size(events.getSize())
                .build();
        return ResponseEntity.ok(
                ApiResponse.<PagedResponse<EventDto>>builder()
                        .message("Event fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<EventDto>> updateEvent (
            @Valid @RequestBody EventRequestPrims req
    ){
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event updated successfully")
                        .data(eventService.updateEvent(req))
                        .build()
        );
    }

    @PutMapping("/publish/{eventID}")
    public ResponseEntity<ApiResponse<EventDto>> publishEvent (
            @Valid @RequestBody Long organizerID,
            @PathVariable  Long eventID
    ){
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event published successfully")
                        .data(eventService.publishEvent(organizerID, eventID))
                        .build()
        );
    }

    @PutMapping("/cancel/{eventID}")
    public ResponseEntity<ApiResponse<EventDto>> cancelEvent (
            @Valid @RequestBody Long organizerID,
            @PathVariable  Long eventID
    ){
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event cancelled successfully")
                        .data(eventService.cancelEvent(organizerID, eventID))
                        .build()
        );
    }

    @PutMapping("/complete/{eventID}")
    public ResponseEntity<ApiResponse<EventDto>> completeEvent (
            @PathVariable  Long eventID
    ){
        return ResponseEntity.ok(
                ApiResponse.<EventDto>builder()
                        .message("Event completed successfully")
                        .data(eventService.completeEvent(eventID))
                        .build()
        );
    }

}
