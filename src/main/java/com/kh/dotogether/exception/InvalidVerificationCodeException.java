package com.kh.dotogether.exception;

// 이메일 코드 인증 예외
public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}