package org.core.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MailRequest(
        @NotBlank(message = "전송할 이메일 입력하세요")
        String email,
        @NotBlank(message = "메일 제목을 입력하세요")
        String subject,
        @NotBlank(message = "메일 본문을 입력하세요")
        String message
) {}
