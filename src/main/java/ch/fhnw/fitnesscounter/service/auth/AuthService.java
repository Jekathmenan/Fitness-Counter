package ch.fhnw.fitnesscounter.service.auth;

import ch.fhnw.fitnesscounter.dto.auth.*;
import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.PasswordResetToken;
import ch.fhnw.fitnesscounter.model.auth.Role;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailService mailService;

    /**
     *
     * Diese Methode registriert einen neuen Benutzer.
     *
     */
    public void register (RegisterRequest registerRequest) {
        log.info("Versuch einer Registrierung für E-Mail: {}", registerRequest.email());

        // Prüfe, ob Passwörter übereinstimmen
        if (!Objects.equals(registerRequest.password(), registerRequest.retypePassword())) {
            log.debug("Eingegeben Passwörter stimmen nicht überein. Betroffene E-Mail: {}", registerRequest.email());
            throw new FitnessAPIException("Passwörter müssen übereinstimmen", "password");
        }

        // Prüfe, ob die E-Mail-Adresse vergeben ist
        if (userService.findByEmail(registerRequest.email()).isPresent()) {
            log.warn("E-Mail ist bereits vergeben: {}", registerRequest.email());
            throw new FitnessAPIException("E-Mail ist bereits vergeben", "email");
        }

        // Speichere den Benutzer in der Datenbank.
        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .build();

        userService.save(user);
        log.info("User erfolgreich registriert: {}", registerRequest.email());
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
        User user = userService.findByEmailOrThrow(loginRequest.email());

        // Melde den Benutzer an
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            // Generiere den AuthToken und gebe ihn zurück
            String token = tokenService.generateToken(authentication, user.isResetPassword());
            log.info("User {} hat sich erfolgreich eingeloggt.", loginRequest.email());
            return new LoginResponse(token, user.isResetPassword());
        } catch (BadCredentialsException ex) {
            log.warn("Login-Fehlschlag: Ungültiges Passwort für Konto {}", loginRequest.email());
            throw new FitnessAPIException(ex.getMessage(), "password");
        }
        catch (Exception ex) {
            log.warn("Kritischer Fehler beim Login-Prozess für {}.", loginRequest.email(), ex);
            throw new FitnessAPIException(ex.getMessage(), "password");
        }
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
        if (!Objects.equals(request.newPassword(), request.retypePassword())) {
            log.debug("Eingegeben Passwörter stimmen nicht überein.");
            throw new FitnessAPIException("Passwörter stimmen nicht überein");
        }

        // Suche den Benutzer in der Datenbank
        User user = userService.findByEmailOrThrow(email);

        // Setze das neue Passwort
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPassword(false);
        userService.save(user);
    }

    /**
     *
     * Sendet die Reset Password Link per E-Mail
     *
     * @param email
     */
    public void sendResetPasswordEmail (String email) {
        User user = userService.findByEmailOrThrow(email);

        // Lösche allfällige alte Tokens
        tokenRepository.deleteByUser(user);
        tokenRepository.flush();

        // Generiere ein neues Reset-Token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);
        mailService.sendResetMail(user.getEmail(), token);

        log.info("Reset-Password Token für User {} generiert und verschickt. ", user.getEmail());
    }

    /**
     *
     * Diese Methode setzt das Passwort des Benutzers zurück.
     *
     * @param request
     */
    public void resetPassword(ResetPasswordRequest request) {
        // Validiere eingegebenen Passwörter
        if (!Objects.equals(request.newPassword(), request.retypePassword())) {
            log.debug("Eingegeben Passwörter stimmen nicht überein. Betroffenes Token: {}", request.token());
            throw new FitnessAPIException("Passwörter stimmen nicht überein", HttpStatus.UNAUTHORIZED);
        }

        // Prüfe den resetToken
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token()).orElseThrow(() -> {
            log.warn("Zugriffsversuch gescheitert: Token {} war ungültig. ", request.token());
            return new FitnessAPIException("Der Reset-Token ist ungültig.", HttpStatus.UNAUTHORIZED);
        });

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            log.warn("Zugriffsversuch gescheitert: Token {} ist abgelaufen. ", request.token());
            throw new FitnessAPIException("Der Reset-Token ist abgelaufen", HttpStatus.UNAUTHORIZED);
        }

        // Setzt das Passwort zurück
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetPassword(false);
        userService.save(user);
        tokenRepository.delete(resetToken);
        log.info("Passwort geändert für Benutzer {}. ", user.getEmail());
    }
}