package org.api.exception;

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
    private URI type = BLANK_TYPE;

    private String detail;

    private String title;

    @Nullable
    private String instance;


    public ErrorDto(ErrorCodes errorCode, @Nullable URI type, @Nullable String instance) {
        this.title = errorCode.toString();
        this.type = type;
        this.detail = errorCode.getDetail();
        this.instance = instance;
    }
}
