package com.kh.dotogether.password.service;

public interface PasswordService {

	/**
	 * 비밀번호 암호화
	 * @param rawPassword
	 * @return 암호화된 비밀번호
	 */
	String encodePassword(String rawPassword);
	
	/**
	 * 비밀번호 일치 여부 확인
	 * @param rawPassword
	 * @param encodedPassword
	 * @return r -> 일치, f -> 불일치
	 */
	boolean matches(String rawPassword, String encodedPassword);
}
