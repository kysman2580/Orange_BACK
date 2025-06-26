package com.kh.dotogether.exception.exceptions;

import com.kh.dotogether.global.enums.ErrorCode;

public class CustomException extends RuntimeException{
	
	private final ErrorCode errorCode;
	
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
