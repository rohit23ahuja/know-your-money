package com.kym.controller;

import com.kym.api.LoginRequest;
import com.kym.api.LoginResponse;
import com.kym.service.JwtService;
import com.kym.service.KymUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final KymUserDetailsService kymUserDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, KymUserDetailsService kymUserDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.kymUserDetailsService = kymUserDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        UserDetails userDetails = kymUserDetailsService.loadUserByUsername(loginRequest.username());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
