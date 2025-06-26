package com.kh.dotogether.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INSERT_ERROR("E001", "데이터 저장중 예외발생"),
	UPDATE_ERROR("E002", "데이터 수정중 예외발생");
	
	private final String code;
    private final String message;
}
