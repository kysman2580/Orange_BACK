package com.kh.dotogether.exception;

// 정보 업데이트 실패 예외처리 클래스
public class UserUpdateFailedException extends RuntimeException {
	
	public UserUpdateFailedException() {
        super("회원 정보 업데이트에 실패했습니다.");
    }

    public UserUpdateFailedException(String message) {
        super(message);
    }

    public UserUpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
