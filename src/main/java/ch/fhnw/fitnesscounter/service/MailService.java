package ch.fhnw.fitnesscounter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    /**
     *
     * Sendet Passwort Zurücksetzen E-Mails
     *
     * @param to
     * @param token
     */
    public void sendResetMail(String to, String token) {
        // TODO: Eine Möglichkeit überlegen, um diese URL dynamisch aufzubauen: https://URL_DER_FRONTENDAPP/reset-password?token=TOKEN.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("FitnessCounter - Passwort zurücksetzen");
        message.setText("Nutzen Sie folgenden Token, um Ihr Passwort zu ändern: " + token +
                "\nOder klicken Sie hier: http://localhost:8082/reset-password?token=" + token);
        mailSender.send(message);
    }
}
