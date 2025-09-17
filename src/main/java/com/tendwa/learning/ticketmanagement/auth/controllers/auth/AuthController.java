package com.tendwa.learning.ticketmanagement.auth.controllers.auth;

import com.tendwa.learning.ticketmanagement.auth.dtos.ResetPasswordRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.logins.JwtResponse;
import com.tendwa.learning.ticketmanagement.auth.dtos.logins.LoginRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.role.AssignRoleRequest;
import com.tendwa.learning.ticketmanagement.auth.dtos.user.UserDto;
import com.tendwa.learning.ticketmanagement.auth.mappers.UserMapper;
import com.tendwa.learning.ticketmanagement.auth.repositories.UserRepository;
import com.tendwa.learning.ticketmanagement.auth.services.UserService;
import com.tendwa.learning.ticketmanagement.auth.services.impl.AuthServiceImpl;
import com.tendwa.learning.ticketmanagement.auth.services.impl.JwtServiceImpl;
import com.tendwa.learning.ticketmanagement.generic.config.JwtConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name="Auth Controllers", description = "APIs to run Authentication logic")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final UserService userService;
    private final AuthServiceImpl authServiceImpl;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public JwtResponse login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
            ){

        var loginResult = authServiceImpl.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());
    }

    @GetMapping("/refresh")
    public JwtResponse refresh(
            @CookieValue(name = "refresh_token", defaultValue = "my default value") String refreshToken,
            HttpServletResponse response
    ){
        var accessToken = authServiceImpl.refreshAccessTokenFn(refreshToken);
        var claims = jwtServiceImpl.parseToken(accessToken.toString());
        var user = userRepository.findById(claims.getUserId()).orElseThrow();
        var newRefreshToken = jwtServiceImpl.generateRefreshToken(user);
        System.out.println("Refresh Token: " + newRefreshToken);
        var cookie = new Cookie("refresh_token", newRefreshToken.toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);

        return new JwtResponse(accessToken.toString());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var user = authServiceImpl.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/assign")
    public ResponseEntity<?> assignUserRole(
            @RequestBody AssignRoleRequest req
            ){
        userService.assignRole(req.getUserId(), req.getRoleId());
        return ResponseEntity.ok().body(
                Map.of("message", "Successfully assigned role")
        );
    }
    @PostMapping("/password-reset")
    public ResponseEntity<?> passwordReset(
            @RequestBody ResetPasswordRequest req
            ){
        var res = userService.resetPassword(req.getResetToken(), req.getNewPassword());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/pass-reset-init")
    public ResponseEntity<?> passResetInit(
            @RequestBody String email
    ){
        var res = userService.requestPasswordReset(email);
        return ResponseEntity.ok().body(res);
    }

}





