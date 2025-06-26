package com.kh.dotogether.member.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.dotogether.auth.util.EncryptionUtil;
import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.exception.DuplicateUserException;
import com.kh.dotogether.exception.UserNotFoundException;
import com.kh.dotogether.exception.UserUpdateFailedException;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.member.model.dto.UserIdResponseDTO;
import com.kh.dotogether.password.service.PasswordService;
import com.kh.dotogether.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final PasswordService passwordService; // 비밀번호 암호화(Argon2)
	private final TokenService tokenService;
	private final EncryptionUtil encryptionUtil; // 양방향 암호화
	private final JWTUtil jwtUtil;

	@Override
	public void signUp(MemberDTO memberDTO) {
		// 중복 체크
		if(isUserIdDuplicated(memberDTO.getUserId())) {
			throw new DuplicateUserException("이미 존재하는 아이디입니다.");
		}
		if(isEmailDuplicated(memberDTO.getUserEmail())) {
			throw new DuplicateUserException("이미 존재하는 이메일입니다.");
		}
		if(isPhoneDuplicated(memberDTO.getUserId())) {
			throw new DuplicateUserException("이미 존재하는 연락처입니다.");
		}
		
		// 패스워드 암호화(일방향)
		memberDTO.setUserPw(passwordService.encodePassword(memberDTO.getUserPw()));
		
		// 연락처, 이메일 암호화 저장(양방향)
		memberDTO.setUserPhone(encryptionUtil.encrypt(memberDTO.getUserPhone()));
		memberDTO.setUserEmail(encryptionUtil.encrypt(memberDTO.getUserEmail()));
		
		// 기본값 설정
		memberDTO.setUserRole("USER");
		memberDTO.setUserStatus("Y");
		
		// 회원가입 진행
		memberMapper.signUp(memberDTO);
		
		log.info("회원가입 완료: userId = {}", memberDTO.getUserId());
	}

	@Override
	public boolean isUserIdDuplicated(String userId) {
		return memberMapper.existsByUserId(userId) > 0;
	}

	@Override
	public boolean isEmailDuplicated(String email) {
		String encEmail = encryptionUtil.encrypt(email);
		return memberMapper.existsByEmail(encEmail) > 0;
	}

	@Override
	public boolean isPhoneDuplicated(String phone) {
		String encPhone = encryptionUtil.encrypt(phone);
		return memberMapper.existsByPhone(encPhone) > 0;
	}

	@Override
	@Transactional
	public boolean deleteUser(Long userNo, String authorizationHeader) {
		// 토큰 유효성 검사 및 사용자 인증
		String token = extractToken(authorizationHeader);
		String userIdFromToken = jwtUtil.getUserIdFromToken(token);
		
		MemberDTO member = getValidMember(userNo);
		
		if(!member.getUserId().equals(userIdFromToken)) {
			throw new IllegalArgumentException("본인 계정만 탈퇴할 수 있습니다.");
		}
		
		// userStatus = 'N'으로 변경
		int result = memberMapper.deleteUser(member.getUserNo());
		
		// 리프레시 토큰 삭제
		tokenService.deleteUserToken(userNo);
		log.info("회원탈퇴 완료: userId = {}", userNo);
		
		return result > 0;
	}

	@Override
	public MemberDTO findByUserNo(Long userNo) {
		MemberDTO member = getValidMember(userNo);
		
		// 연락처, 이메일 복호화
		member.setUserPhone(encryptionUtil.decrypt(member.getUserPhone()));
		member.setUserEmail(encryptionUtil.decrypt(member.getUserEmail()));
		
		return member;
	}

	@Override
	public void resetPassword(Long userNo, String email, String newPassword) {
		MemberDTO member = getValidMember(userNo);
		
		// 이메일 비교는 복호화 후 비교
		String dbEmail = encryptionUtil.decrypt(member.getUserEmail());
		if(!dbEmail.equals(email)) {
			throw new UserNotFoundException("일치하는 이메일이 없습니다.");
		}
		
		// 비밀번호 암호화 후 변경
		String encodeNewPassword = passwordService.encodePassword(newPassword);
		int result = memberMapper.updatePassword(userNo, encodeNewPassword);
		
		if(result != 1) {
			log.error("비밀번호 업데이트 실패: userId = {}, email = {}", userNo, email);
			throw new UserUpdateFailedException("비밀번호 업데이트 실패");
		}
		log.info("비밀번호 재설정 완료: userId = {}, email = {}", userNo, email);
	}
	
	private MemberDTO getValidMember(Long userNo) {
	    MemberDTO member = memberMapper.findByUserNo(userNo);
	    if (member == null) {
	        throw new UserNotFoundException("존재하지 않는 사용자입니다.");
	    }
	    return member;
	}
	
	private String extractToken(String authorizationHeader) {
		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new IllegalArgumentException("유효한 인증 정보가 없습니다");
		}
		return authorizationHeader.substring(7); // "Bearer " 제거
	}

	
	/**
	 * 아이디 찾기(이름, 이메일)
	 */
	@Override
	public UserIdResponseDTO findUserId(String userName, String userEmail) {
		MemberDTO member = memberMapper.findByName(userName);

	    if (member == null) {
	        throw new UserNotFoundException("존재하지 않는 회원입니다.");
	    }
		
	    // 복호화
	    String decryptedEmail = encryptionUtil.decrypt(member.getUserEmail());

	    if (!userEmail.equals(decryptedEmail)) {
	        throw new UserNotFoundException("이메일이 일치하지 않습니다.");
	    }

	    log.info("아이디 찾기 성공 - userId: {}", member.getUserId());
	    return new UserIdResponseDTO(member.getUserId());
	}

	/**
	 * 비밀번호 찾기 1단계 - 아이디 조회
	 */
	@Override
	public MemberDTO findByUserId(String userId) {
		MemberDTO member = memberMapper.findByUserId(userId);

	    if (member == null) {
	        throw new UserNotFoundException("존재하지 않는 아이디입니다.");
	    }
		return memberMapper.findByUserId(userId);
	}


}
