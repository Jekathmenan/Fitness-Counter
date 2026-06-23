package ch.fhnw.fitnesscounter.config;

import ch.fhnw.fitnesscounter.model.auth.Role;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * Diese CommandLineRunner-Implementation ist für das einmalige Erstellen des Admin-Benutzers zuständig!
 *
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@fitness.ch").isEmpty()) {
            User admin = User.builder()
                    .email("admin@fitness.ch")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Admin")
                    .resetPassword(true)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("--- Admin-User initialisiert ---");
        }
    }
}