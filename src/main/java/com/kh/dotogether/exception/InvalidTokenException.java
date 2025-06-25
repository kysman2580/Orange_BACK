package com.kh.dotogether.exception;

public class InvalidTokenException extends RuntimeException{
	public InvalidTokenException(String message) {
		super(message);
	}
}
