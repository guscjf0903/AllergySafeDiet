package org.core.request;


public record VerifyCodeRequest(
        String email,
        Long userPk,
        String verificationCode
) {}