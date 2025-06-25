package com.kh.dotogether.exception;

// 아이디, 이메일, 연락처 중복 예외처리 클래스
public class DuplicateUserException extends RuntimeException{
	public DuplicateUserException() {
        super("중복된 사용자 정보가 존재합니다.");
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}
