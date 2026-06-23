package ch.fhnw.fitnesscounter.repository;

import ch.fhnw.fitnesscounter.model.auth.PasswordResetToken;
import ch.fhnw.fitnesscounter.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<PasswordResetToken> findByUser(User user);
}
