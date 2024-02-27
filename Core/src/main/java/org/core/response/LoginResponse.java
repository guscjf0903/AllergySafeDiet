package org.core.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String loginToken;

    @JsonCreator
    public LoginResponse(@JsonProperty("loginToken") String loginToken) {
        this.loginToken = loginToken;
    }
}
