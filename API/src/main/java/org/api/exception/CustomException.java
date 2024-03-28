package org.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCodes errorCode;
    private final Object additionalData;

    public CustomException(ErrorCodes errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
        this.additionalData = null;
    }

    public CustomException(ErrorCodes errorCode, Object additionalData) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
        this.additionalData = additionalData;
    }
}
