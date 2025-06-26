package com.kh.dotogether.exception.exceptions;

import com.kh.dotogether.global.enums.ErrorCode;

public class CustomException extends RuntimeException{
	
	private final String code;
    private final String message;
	
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
	}
	
	public CustomException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

	
	public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
