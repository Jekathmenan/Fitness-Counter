package ch.fhnw.fitnesscounter.config;

import ch.fhnw.fitnesscounter.dto.coreData.BodyPartsListDto;
import ch.fhnw.fitnesscounter.model.auth.Role;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.repository.BodyPartsRepository;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import ch.fhnw.fitnesscounter.service.coreData.BodyPartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

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
    private final BodyPartsRepository bodyPartsRepository;
    private final BodyPartsService bodyPartsService;
    private final ObjectMapper objectMapper;

    /**
     *
     * Diese Methode ist für das einmalige seeden der Datenbank mit einem Admin-Benutzer zuständig.
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        // Erstelle den Admin-Benutzer, wenn dieser nicht schon existiert
        seedAdminUser();
        seedDefaultBodyParts();
    }

    private void seedAdminUser() {
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
        }
    }

    /**
     * Diese Methode lädt die Körperteile aus der JSON-Datei /data/bodyparts.json in die Datenbank.
     */
    private void seedDefaultBodyParts() {
        if (bodyPartsRepository.count() == 0) {
            // Lade JSON File
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/bodyparts.json")) {
                // Mappe JSON-Stream als BodyPartsListDto
                BodyPartsListDto bodyPartsListDto = objectMapper.readValue(inputStream, BodyPartsListDto.class);

                // Übergebe bodyPartList an den Service
                bodyPartsService.createMany(bodyPartsListDto.getBodyParts(), "admin@fitness.ch");
            } catch (Exception e) {
                throw new RuntimeException("Fehler beim Füllen der Datenbank mit Initialdaten.", e);
            }
        }
    }
}