package ch.fhnw.fitnesscounter.controller.auth;

import ch.fhnw.fitnesscounter.dto.auth.*;
import ch.fhnw.fitnesscounter.service.auth.AuthService;
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

    /**
     *
     * Dieser Endpunkt erlaubt die Registrierung eines neuen Benutzers.
     * Gibt bei Erfolg den HTTPStatus 201 - Created zurück.
     *
     * @param registerRequest
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    /**
     *
     * Dieser Endpunkt erlaubt das Anmelden eines Benutzers unter Angabe der im Login-DTO definierten Attribute
     * (E-Mail, Passwort).
     * Die mitgegebenen Werte werden in der DTO validiert und bei Fehler die entsprechende Meldung zurückgegeben.
     * Bei Erfolg wird ein Bearer Token zurückgegeben, der für eine Stunde gültig ist.
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     *
     * Diese Methode erlaubt einen Admin Benutzer sein standard mässig vom System vorgegebenes Passwort zu ändern.
     * Der Admin Benutzer muss angemeldet sein und seinen Bearer Token im Header mitgeben, damit diese Route funktioniert.
     *
     * @param request
     * @param principal
     */
    @PostMapping("/update-password")
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request, Principal principal) {
        authService.updateInitialPassword(principal.getName(), request);
    }

    /**
     *
     * Diese Methode erlaubt einem Benutzer anzufragen, sein Passwort zurückzusetzen.
     * Bei Erfolg wird der Response Code 202 - Accepted UND eine E-Mail an mit dem Reset-Token an die Adresse des
     * Benutzers geschickt
     *
     * @param email
     * @return
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.sendResetPasswordEmail(email);
        return ResponseEntity.accepted().build();
    }

    /**
     *
     * Diese Methode erlaubt einen Benutzer sein Passwort zurückzusetzen. Ein gültiger Token muss im HTTP-Body
     * mitgeschickt werden.
     *
     * @param request
     * @return
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword (@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.status(200).build();
    }
}