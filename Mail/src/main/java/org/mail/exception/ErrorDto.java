package org.mail.exception;

import jakarta.annotation.Nullable;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto {
    private static final URI BLANK_TYPE = URI.create("about:blank");

    @Nullable
    private URI type;
    private String detail;
    private String title;
    @Nullable
    private String instance;
    @Nullable
    private Object additionalData;

    public ErrorDto(ErrorCodes errorCode, URI type, String instance) {
        this(errorCode, type, instance, null);
    }

    public ErrorDto(ErrorCodes errorCode, @Nullable URI type, @Nullable String instance, @Nullable Object additionalData) {
        this.title = errorCode.toString();
        this.type = (type != null) ? type : BLANK_TYPE;
        this.detail = errorCode.getDetail();
        this.instance = instance;
        this.additionalData = additionalData;
    }

}
