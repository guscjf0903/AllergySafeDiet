package org.mail.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
    FAILED_MAIL_SEND(500, "메일 전송 중 실패했습니다.");

    private final int status;
    private final String detail;

}
