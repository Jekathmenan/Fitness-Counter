package ch.fhnw.fitnesscounter;

import ch.fhnw.fitnesscounter.dto.auth.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class FitnesscounterApplication {
	public static void main(String[] args) {
		SpringApplication.run(FitnesscounterApplication.class, args);
	}

}
