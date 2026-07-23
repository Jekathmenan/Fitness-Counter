package ch.fhnw.fitnesscounter.service.auth;

import ch.fhnw.fitnesscounter.exception.FitnessAPIException;
import ch.fhnw.fitnesscounter.model.auth.User;
import ch.fhnw.fitnesscounter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * Dieser Service ist die Abstraktion der UserRepository. Sie erweitert das UserRepository mit der
 * Funktion findByEMailOrThrow
 *
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     *
     * Gibt den gefundenen User zurück. Ansonsten wird eine Exception geworfen.
     *
     * @param email
     * @return
     */
    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("Zugriffsversuch gescheitert: Benutzer {} nicht gefunden.", email);
            return new FitnessAPIException("User nicht gefunden");
        });
    }

    public void save(User user) {
        this.userRepository.save(user);
    }
}