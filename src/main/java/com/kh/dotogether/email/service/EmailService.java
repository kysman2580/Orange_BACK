package com.kh.dotogether.email.service;

public interface EmailService {
	
	/**
	 * 이메일 인증 코드 발송
	 * @param email
	 * @param verificationCode
	 */
	void sendVerificationEmail(String toEmail, String verificationCode);

}
