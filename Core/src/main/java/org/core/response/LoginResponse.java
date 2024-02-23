package org.core.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final Long loginToken;

    @JsonCreator
    public LoginResponse(@JsonProperty("loginToken") Long loginToken) {
        this.loginToken = loginToken;
    }
}
