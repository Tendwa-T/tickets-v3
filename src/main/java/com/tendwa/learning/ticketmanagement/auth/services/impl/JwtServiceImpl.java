package com.tendwa.learning.ticketmanagement.auth.services.impl;

import com.tendwa.learning.ticketmanagement.auth.entities.Jwt;
import com.tendwa.learning.ticketmanagement.auth.entities.User;
import com.tendwa.learning.ticketmanagement.auth.repositories.UserRoleRepository;
import com.tendwa.learning.ticketmanagement.generic.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JwtServiceImpl {
    private final JwtConfig jwtConfig;
    private final UserRoleRepository userRoleRepository;

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private Jwt generateToken(User user, long tokenExpiration){
        //Fetch the roles from the user_roles table then append them
        var userRoles = userRoleRepository.findByUser_Id(user.getId());

        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("role", userRoles.stream()
                        .map(userRole -> userRole.getRole().getName())  // extract role names
                        .collect(Collectors.toList())                   // convert to List<String>
                )
                .add("name",  user.getFirstName() + user.getLastName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
                .build();


        return new Jwt(claims,jwtConfig.getSecretKey());
    }

    public Jwt parseToken(String token){
        try{
            var claims = getClaims(token);
            return new Jwt(claims,jwtConfig.getSecretKey());
        }catch(JwtException e){
            return null;
        }
    }

    public Jwt generateAccessToken(User user){
        final long tokenExpiration = jwtConfig.getAccessTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    public Jwt generateRefreshToken(User user){
        final long tokenExpiration = jwtConfig.getRefreshTokenExpiration();
        return generateToken(user, tokenExpiration);
    }

    public Jwt generateRedirectToken(User user){
        final long tokenExpiration = jwtConfig.getRedirectTokenExpiration();
        return generateToken(user, tokenExpiration);
    }
}
