package org.core.response;

public record SignupResponse(
        Long userPk
) {
    public static SignupResponse toResponse(Long userPk) {
        return new SignupResponse(userPk);
    }

}