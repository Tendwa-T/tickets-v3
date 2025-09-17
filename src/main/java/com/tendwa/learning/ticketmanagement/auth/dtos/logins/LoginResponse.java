package com.tendwa.learning.ticketmanagement.auth.dtos.logins;

import com.tendwa.learning.ticketmanagement.auth.entities.Jwt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}
