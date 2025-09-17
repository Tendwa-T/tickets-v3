package com.tendwa.learning.ticketmanagement.cart.controllers;

import com.tendwa.learning.ticketmanagement.cart.dtos.AddItemToCartRequest;
import com.tendwa.learning.ticketmanagement.cart.dtos.CartDto;
import com.tendwa.learning.ticketmanagement.cart.dtos.CartItemDto;
import com.tendwa.learning.ticketmanagement.cart.dtos.UpdateCartItemRequest;
import com.tendwa.learning.ticketmanagement.cart.services.CartService;
import com.tendwa.learning.ticketmanagement.generic.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "Create a cart")
    public ResponseEntity<ApiResponse<CartDto>> createCart(){
        return ResponseEntity.ok(
                ApiResponse.<CartDto>builder()
                        .message("Cart Created")
                        .data(cartService.createCart())
                        .build()
        );
    }

    @PostMapping("/{cartID}/items")
    public ResponseEntity<ApiResponse<CartItemDto>> addItem(
            @PathVariable UUID cartID,
            @RequestBody AddItemToCartRequest request
    ){
        return ResponseEntity.ok(
                ApiResponse.<CartItemDto>builder()
                        .message("Ticket Added Successfully")
                        .data(cartService.addToCart(cartID, request.getTicketID()))
                        .build()
        );
    }

    @GetMapping("/{cartID}")
    public ResponseEntity<ApiResponse<CartDto>> getCartItem(
            @PathVariable UUID cartID
    ){
        return ResponseEntity.ok(
                ApiResponse.<CartDto>builder()
                        .message("Cart Fetched Successfully")
                        .data(cartService.getCart(cartID))
                        .build()
        );
    }

    @PutMapping("/{cartID}/items/{ticketID}")
    public ResponseEntity<ApiResponse<CartItemDto>> updateItem(
            @PathVariable UUID cartID,
            @PathVariable Long ticketID,
            @Valid @RequestBody UpdateCartItemRequest request
    ){
        return ResponseEntity.ok(
                ApiResponse.<CartItemDto>builder()
                        .message("Cart Fetched Successfully")
                        .data(cartService.updateCartItem(cartID, ticketID, request.getQuantity()))
                        .build()
        );
    }

    @DeleteMapping("/{cartID}/items/{ticketID}")
    @Operation(summary = "Delete items from cart")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @PathVariable UUID cartID,
            @PathVariable Long ticketID
    ){
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Item Deleted Successfully")
                        .build()
        );
    }

    @DeleteMapping("/{cartID}/items")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @PathVariable UUID cartID
    ){
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Cart Cleared")
                        .build()
        );
    }

}










