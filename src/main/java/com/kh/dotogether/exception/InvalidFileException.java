package com.kh.dotogether.exception;

// 프로필 사진 등록 예외
public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String message) {
        super(message);
    }
}