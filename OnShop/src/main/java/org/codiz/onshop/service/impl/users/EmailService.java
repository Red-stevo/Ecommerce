package org.codiz.onshop.service.impl.users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class EmailService {
    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;

    public EmailService(ResourceLoader resourceLoader, JavaMailSender mailSender) {
        this.resourceLoader = resourceLoader;
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String to, String password) {
        try {
            // Load HTML file from resources
            String emailBody = loadEmailTemplate();

            // Replace {{ password }} with the actual password
            String personalizedBody = emailBody.replace("{{ password }}", password);

            // Send the email
            sendEmail(to, personalizedBody);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load email template", e);
        }
    }

    private String loadEmailTemplate() throws IOException {
        // Load the HTML file from the resources/templates folder
        return new String(Files.readAllBytes(Paths.get(resourceLoader.getResource("classpath:Templates/Email.html").getURI())), StandardCharsets.UTF_8);
    }

    public void sendEmail(String to, String emailBody) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setTo(to);
            helper.setSubject("Maseno Medical Lab Club Registration Details");
            helper.setSentDate(new Date());
            helper.setText(emailBody, true);  // true indicates HTML content

            // Send the email
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
