package com.tendwa.learning.ticketmanagement.auth.dtos.logins;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
