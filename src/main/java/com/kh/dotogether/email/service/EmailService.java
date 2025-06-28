package com.kh.dotogether.email.service;

import org.springframework.http.ResponseEntity;

import com.kh.dotogether.util.ResponseData;

public interface EmailService {
	
	/**
	 * 이메일 인증 코드 발송
	 * @param email
	 * @param verificationCode
	 */
	void sendVerificationEmail(String email, String code);
	
	/**
     * 비밀번호 재설정용 인증 전체 처리
     */
	ResponseEntity<ResponseData> processPasswordResetVerification(String userId, String userEmail);

	ResponseEntity<ResponseData> verifyEmailCode(String email, String code);

}
