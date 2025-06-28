package com.kh.dotogether.email.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.util.EncryptionUtil;
import com.kh.dotogether.email.dao.EmailMapper;
import com.kh.dotogether.email.dto.EmailVerificationDTO;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.util.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	private final EmailMapper emailMapper;
	private final EncryptionUtil encryptionUtil;
	private final MemberMapper memberMapper;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	// 이메일 발송
	@Override
	public void sendVerificationEmail(String userEmail, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(userEmail);
		message.setFrom(fromEmail);
		message.setSubject("[DoTogether] 이메일 인증 코드 안내");
		message.setText("인증 코드: " + code);
		
		mailSender.send(message);
		log.info("이메일 인증 코드 발송 완료 -> {}", userEmail);
	}

	@Override
	public ResponseEntity<ResponseData> processPasswordResetVerification(String userId, String userEmail) {
		if (!checkUserExists(userId, userEmail)) {
            return buildErrorResponse("일치하는 회원 정보를 찾을 수 없습니다.");
        }

        String code = generateVerificationCode();
        saveVerificationCode(userId, userEmail, code);
        sendVerificationEmail(userEmail, code);

        return buildSuccessResponse(userEmail, code);
	}
	
	// 사용자 존재 여부 확인
	private boolean checkUserExists(String userId, String userEmail) {
		String encryptedEmail = encryptionUtil.encrypt(userEmail);
		return emailMapper.existsByUserIdAndEmail(userId, encryptedEmail);
	}
	
	// 4자리 랜덤 인증번호 생성
	private String generateVerificationCode() {
		return String.format("%04d", (int) (Math.random() * 10000));
	}
	
	// 인증 번호 저장
	private void saveVerificationCode(String userId, String userEmail, String code) {
		MemberDTO member = memberMapper.findByUserId(userId);
	    if (member == null) {
	        throw new CustomException(ErrorCode.NOT_FOUND_USER);
	    }
	    Long userNo = member.getUserNo();
	    
		LocalDateTime expireAt = LocalDateTime.now().plusMinutes(3);
		
	    emailMapper.deleteVerificationByEmail(userEmail); // 기존 이메일 삭제
	    emailMapper.insertVerification(userNo, userEmail, code, expireAt); // 새로 저장
	}
	
	// 성공 응답 포맷
    private ResponseEntity<ResponseData> buildSuccessResponse(String email, String code) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code("200")
                        .message("이메일 인증 코드 전송 완료")
                        .items(List.of(Map.of(
                                "userEmail", email,
                                "code", code
                        )))
                        .build()
        );
    }

    // 실패 응답 포맷
    private ResponseEntity<ResponseData> buildErrorResponse(String message) {
        return ResponseEntity.badRequest().body(
                ResponseData.builder()
                        .code("400")
                        .message(message)
                        .items(Collections.emptyList())
                        .build()
        );
    }

	@Override
	public ResponseEntity<ResponseData> verifyEmailCode(String email, String code) {
		EmailVerificationDTO stored = emailMapper.findVerificationByEmail(email);

	    if (stored == null) {
	        return buildErrorResponse("인증 내역이 존재하지 않습니다.");
	    }
	    if (!stored.getCode().equals(code)) {
	        return buildErrorResponse("인증번호가 일치하지 않습니다.");
	    }
	    if (LocalDateTime.now().isAfter(stored.getExpireAt())) {
	        return buildErrorResponse("인증번호가 만료되었습니다.");
	    }

	    emailMapper.deleteVerificationByEmail(email); // 인증코드 삭제

	    return ResponseEntity.ok(
	            ResponseData.builder()
	                    .code("200")
	                    .message("이메일 인증 완료")
	                    .items(Collections.emptyList())
	                    .build()
	    );
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
