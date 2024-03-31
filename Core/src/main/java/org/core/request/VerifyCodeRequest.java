package org.core.request;


public record VerifyCodeRequest(
        String email,
        String userPk,
        String verificationCode
) {}