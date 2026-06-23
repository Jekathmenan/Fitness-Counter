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

    /**
     *
     * Diese Methode registriert einen neuen Benutzer.
     *
     * @param registerRequest
     */
    public void register (RegisterRequest registerRequest) {
        // Prüfe, ob Passwörter übereinstimmen
        if (!Objects.equals(registerRequest.password(), registerRequest.retypePassword()))
            throw new IllegalArgumentException("Passwörter müssen übereinstimmen");

        // Prüfe, ob die E-Mail-Adresse vergeben ist
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("E-Mail ist bereits vergeben");
        }

        // Speichere den Benutzer in der Datenbank.
        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    /**
     *
     * Diese Methode ist für das Einloggen des Benutzers und zurückgeben des Login-Tokens zuständig.
     *
     * @param loginRequest
     * @return
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Prüfe ob der user existiert
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new RuntimeException("User nicht gefunden"));

        // Melde den Benutzer an
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        // Generiere den AuthToken und gebe ihn zurück
        String token = tokenService.generateToken(authentication, user.isResetPassword());
        return new LoginResponse(token, user.isResetPassword());
    }

    /**
     *
     * Diese Methode ist für das Zurücksetzen des ursprünglich vom System vergebenem Passwort
     * des Adminbenutzers zuständig.
     *
     * @param email
     * @param request
     */
    public void updateInitialPassword (String email, UpdatePasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.retypePassword()))
            throw new IllegalArgumentException("Passwörter müssen übereinstimmen");

        // Suche den Benutzer in der Datenbank
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        // Setze das neue Passwort
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPassword(false);
        userRepository.save(user);
    }
}