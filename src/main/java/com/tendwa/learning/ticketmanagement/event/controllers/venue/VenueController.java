package com.tendwa.learning.ticketmanagement.event.controllers.venue;

import com.tendwa.learning.ticketmanagement.event.dtos.createDtos.VenueRequestPrims;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueAvailabilityParams;
import com.tendwa.learning.ticketmanagement.event.dtos.venue.VenueDto;
import com.tendwa.learning.ticketmanagement.event.services.VenueService;
import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
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
import org.springframework.web.client.RestClient;

import java.util.Map;

@Tag(name="Venue Controllers", description = "APIs to run Venue logic")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/venue")
@SecurityRequirement(name = "bearerAuth")
public class VenueController {
    private final RestClient.Builder builder;
    private VenueService venueService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VenueDto>> createVenue(
            @Valid @RequestBody VenueRequestPrims req
    ) {
        return ResponseEntity.ok(
                ApiResponse.<VenueDto>builder()
                        .message("Venue Created Successfully")
                        .data(venueService.createVenue(req))
                        .build()
        );
    }

    @PostMapping("/availability")
    public ResponseEntity<ApiResponse<Boolean>> availability(
            @Valid @RequestBody VenueAvailabilityParams req
    ){
        var available = venueService.venueAvailability(req.getVenueID(),req.getStartDate(),req.getEndDate());
        if (available){
            return ResponseEntity.ok(
                    ApiResponse.<Boolean>builder()
                            .message("Venue is Available")
                            .data(true)
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .message("Venue is Booked")
                        .data(false)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<VenueDto>>> getAllVenues(
            @ParameterObject @PageableDefault Pageable pageable
    ){
        return ResponseEntity.ok(
                ApiResponse.<Page<VenueDto>>builder()
                        .message("Venues fetched")
                        .data(venueService.getVenues(pageable))
                        .build()
        );
    }

    @GetMapping("/{venueID}")
    public ResponseEntity<ApiResponse<VenueDto>> getVenueByID(
            @PathVariable Long venueID
    ){
        return ResponseEntity.ok(
                ApiResponse.<VenueDto>builder()
                        .message("Venue fetched")
                        .data(venueService.getVenue(venueID))
                        .build()
        );
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<ApiResponse<Page<VenueDto>>> getVenueByCapacity(
            @PathVariable Integer capacity,
            @ParameterObject @PageableDefault Pageable pageable
    ){
        return ResponseEntity.ok(
                ApiResponse.<Page<VenueDto>>builder()
                        .message("Venues fetched")
                        .data(venueService.getByCapacity(capacity, pageable))
                        .build()
        );
    }


    @PutMapping("/update/{venueID}")
    public ResponseEntity<ApiResponse<VenueDto>> updateVenue(
            @PathVariable Long venueID,
            @Valid @RequestBody VenueRequestPrims req
    ){
        return ResponseEntity.ok(
                ApiResponse.<VenueDto>builder()
                        .message("Venue Updated Successfully")
                        .data(venueService.updateVenue(venueID,req))
                        .build()
        );
    }


    @DeleteMapping("/delete/{venueID}")
    public ResponseEntity<ApiResponse<Void>> deleteVenue(
            @PathVariable Long venueID
    ){
        venueService.deleteVenue(venueID);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Venue Deleted Successfully")
                        .build()
        );
    }

}
