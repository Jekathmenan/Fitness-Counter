package ch.fhnw.fitnesscounter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 * Konfigurationsklasse für die Aktivierung des JPA-Auditings.
 * Ermöglicht die automatische Erfassung von Erstellungs- und Änderungszeitpunkten
 * sowie des jeweiligen Bearbeiters.
 *
 */
@Configuration
@EnableJpaAuditing
public class PersistenceConfig {
}
