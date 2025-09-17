package com.tendwa.learning.ticketmanagement.event.controllers.tickets;


import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.AddTicketsRequest;
import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.TicketTypeRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.ticket.TicketDto;
import com.tendwa.learning.ticketmanagement.event.dtos.ticketType.TicketTypeDto;
import com.tendwa.learning.ticketmanagement.event.services.TicketTypeService;
import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Ticket Controllers", description = "APIs to run Ticket logic")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ticket")
@SecurityRequirement(name = "bearerAuth")
public class ticketController {

    private final TicketTypeService ticketTypeService;

    @PostMapping("/create/ticketTypes")
    public ResponseEntity<ApiResponse<TicketTypeDto>> createTicketTypes(
            @RequestBody TicketTypeRequestPrims ticketTypeRequestPrims
    ) {
        return ResponseEntity.ok(
                ApiResponse.<TicketTypeDto>builder()
                        .message("Successfully created ticket type")
                        .data(ticketTypeService.createTicketType(ticketTypeRequestPrims))
                        .build()
        );
    }

     @GetMapping("/event/{eventID}")
    public ResponseEntity<ApiResponse<Page<TicketTypeDto>>> getTicketTypes(
            @PathVariable Long eventID,
            @ParameterObject @PageableDefault Pageable pageable
     ){
        return ResponseEntity.ok(
                ApiResponse.<Page<TicketTypeDto>>builder()
                        .message("Successfully Fetched ticket type")
                        .data(ticketTypeService.getTicketTypesByEvent(eventID,pageable))
                        .build()
        );
     }

     @PostMapping("/add")
    public ResponseEntity<ApiResponse<List<TicketDto>>> addTickets(
             @RequestBody AddTicketsRequest request
             ){
        return ResponseEntity.ok(
                ApiResponse.<List<TicketDto>>builder()
                        .message("Successfully added ticket")
                        .data(ticketTypeService.addToCart(request.getTicketTypeID(),request.getQuantity()))
                        .build()
        );
     }

}
