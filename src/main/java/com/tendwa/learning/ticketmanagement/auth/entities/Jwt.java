package com.tendwa.learning.ticketmanagement.auth.entities;

import com.tendwa.learning.ticketmanagement.generic.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {return claims.getExpiration().before(new Date());}

    public Long getUserId() {return Long.valueOf(claims.getSubject());}

    public List<RoleEnum> getRoles() {
        List<String> roles = claims.get("role", List.class);
        return roles.stream()
                .map(RoleEnum::valueOf)  // convert each string to RoleEnum
                .toList();
    }

    public String toString() {return Jwts.builder().claims(claims).signWith(secretKey).compact();}

}
