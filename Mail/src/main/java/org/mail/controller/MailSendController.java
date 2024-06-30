package org.mail.controller;

import lombok.RequiredArgsConstructor;
import org.core.request.MailRequest;
import org.mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailSendController {
    private final MailService mailService;
    @PostMapping("/send")
    public ResponseEntity<Void> sendMail(@RequestBody MailRequest mailRequest) {
        mailService.sendMail(mailRequest);

        return ResponseEntity.ok().build();
    }
}
