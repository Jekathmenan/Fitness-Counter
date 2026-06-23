package ch.fhnw.fitnesscounter.controller;

import ch.fhnw.fitnesscounter.dto.auth.*;
import ch.fhnw.fitnesscounter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/update-password")
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request, Principal principal) {
        authService.updateInitialPassword(principal.getName(), request);
    }
}