package com.kh.dotogether.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Override
	public void sendVerificationEmail(String toEmail, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setFrom(fromEmail);
		message.setSubject("[DoTogether] 이메일 인증 코드 안내");
		message.setText("인증 코드: " + verificationCode);
		
		if(true) {
			throw new CustomException(ErrorCode.INSERT_ERROR);
		}
		
		mailSender.send(message);
		log.info("이메일 인증 코드 발송 완료 -> {}", toEmail);
	}
}
