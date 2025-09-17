package com.tendwa.learning.ticketmanagement.order.controllers;

import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
import com.tendwa.learning.ticketmanagement.order.dtos.OrderDto;
import com.tendwa.learning.ticketmanagement.order.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getAllOrders(
            @ParameterObject @PageableDefault Pageable pageable
    ){
        return ResponseEntity.ok(
                ApiResponse.<Page<OrderDto>>builder()
                        .message("Orders Fetched")
                        .data(orderService.getAllOrders(pageable))
                        .build()
        );
    }

    @GetMapping("/{orderID}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @PathVariable Long orderID
    ){
        return ResponseEntity.ok(
                ApiResponse.<OrderDto>builder()
                        .message("Order Fetched")
                        .data(orderService.getOrderById(orderID))
                        .build()
        );
    }



}
