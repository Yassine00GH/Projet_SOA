package com.YassineGhorbel.users.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String smtpPassword;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username:}") String fromEmail,
            @Value("${spring.mail.password:}") String smtpPassword) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.smtpPassword = smtpPassword;
    }

    @Override
    public void sendEmail(String to, String email) {
        if (fromEmail == null || fromEmail.isBlank() || smtpPassword == null || smtpPassword.isBlank()) {
            throw new MailAuthenticationException(
                    "SMTP non configure : definissez MAIL_USERNAME et MAIL_PASSWORD.");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(fromEmail);
            mailSender.send(mimeMessage);
        } catch (org.springframework.mail.MailException e) {
            System.out.println("==================================================================");
            System.out.println("[MODE SECOURS SMTP] Envoi SMTP bloque par le reseau (" + e.getMessage() + ")");
            System.out.println("Destinataire : " + to);
            System.out.println("Contenu      : " + email);
            System.out.println("==================================================================");
        } catch (MessagingException e) {
            throw new IllegalStateException(
                    "Impossible de preparer le courriel. Verifiez MAIL_USERNAME et l'adresse du destinataire.", e);
        }
    }
}

