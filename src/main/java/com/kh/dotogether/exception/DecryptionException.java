package com.kh.dotogether.exception;

// 복호화 실패 예외
public class DecryptionException extends RuntimeException {
    public DecryptionException(String message) {
        super(message);
    }
}