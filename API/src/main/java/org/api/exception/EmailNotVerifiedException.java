package org.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;

@Getter
public class EmailNotVerifiedException extends AuthenticationException {
    // 사용자 고유 키를 반환하는 getter 메서드
    private final String userPk;  // 사용자의 고유 키를 저장할 필드

    public EmailNotVerifiedException(String msg, String userPk) {
        super(msg);
        this.userPk = userPk;
    }

}
