package org.api.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<?> handleCustomException(CustomException ex, HttpServletRequest request) {
        String instance = request.getRequestURL().toString();
        ErrorDto errorDto;
        if (ex.getAdditionalData() != null) {
            errorDto = new ErrorDto(ex.getErrorCode(), null, instance, ex.getAdditionalData());
        } else {
            errorDto = new ErrorDto(ex.getErrorCode(), null, instance);
        }
        log.error(ex.getErrorCode().getDetail());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorDto);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<?> handleServerException() {
        log.error(INTERNAL_SERVER_ERROR.getReasonPhrase());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
