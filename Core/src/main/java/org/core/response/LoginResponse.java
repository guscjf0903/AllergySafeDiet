package org.core.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}
