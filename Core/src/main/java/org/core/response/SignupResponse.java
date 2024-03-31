package org.core.response;

public record SignupResponse(
        String userPk
) {
    public static SignupResponse toResponse(String userPk) {
        return new SignupResponse(userPk);
    }

}