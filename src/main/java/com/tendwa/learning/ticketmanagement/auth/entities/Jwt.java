package com.tendwa.learning.ticketmanagement.auth.entities;

import com.tendwa.learning.ticketmanagement.generic.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {return claims.getExpiration().before(new Date());}

    public Long getUserId() {return Long.valueOf(claims.getSubject());}

    public RoleEnum getRole() {return RoleEnum.valueOf(claims.get("role", String.class));}

    public String toString() {return Jwts.builder().claims(claims).signWith(secretKey).compact();}

}
