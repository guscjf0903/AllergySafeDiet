package org.mail.service;

import lombok.RequiredArgsConstructor;
import org.core.request.MailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendMail(MailRequest mailRequest) {
        SimpleMailMessage mailMessage = createMailMessage(mailRequest.email(), mailRequest.subject(), mailRequest.message());
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
