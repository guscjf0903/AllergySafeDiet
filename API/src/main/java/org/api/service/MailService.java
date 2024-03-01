package org.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    public void sendMail(String email, String subject, String message) {
        SimpleMailMessage mailMessage = createMailMessage(email, subject, message);
        mailSender.send(mailMessage);
    }

    private SimpleMailMessage createMailMessage(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        return mailMessage;
    }
}
