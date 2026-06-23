package ch.fhnw.fitnesscounter.service;

import ch.fhnw.fitnesscounter.dto.auth.*;
import ch.fhnw.fitnesscounter.model.auth.*;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new RuntimeException("User nicht gefunden"));


        String token = tokenService.generateToken(authentication, user.isResetPassword());

        return new LoginResponse(token, user.isResetPassword());
    }
}