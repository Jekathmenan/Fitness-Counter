package ch.fhnw.fitnesscounter.controller;

import ch.fhnw.fitnesscounter.dto.auth.UserResponse;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserResponse getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new FitnessAPIException("User nicht gefunden"));

        return new UserResponse(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}
