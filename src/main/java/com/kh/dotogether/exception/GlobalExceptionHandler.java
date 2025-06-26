package com.kh.dotogether.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.exception.exceptions.InsertFailedException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.util.ResponseError;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private ResponseEntity<ResponseError> exceptionHandler(String code, String Message){
		
		ResponseError responseError = ResponseError.builder().code(code)
							   								 .message(Message)
							   								 .build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ResponseError> handleCustomException(CustomException e){
		ErrorCode errorCode = e.getErrorCode();
		
		return exceptionHandler(errorCode.getCode(), errorCode.getMessage());
	}
	
}
