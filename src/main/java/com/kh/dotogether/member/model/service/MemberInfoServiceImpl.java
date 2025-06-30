package com.kh.dotogether.member.model.service;

import org.springframework.stereotype.Service;

import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.member.model.dto.MypagePasswordUpdateDTO;
import com.kh.dotogether.password.service.PasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
	
	private final MemberMapper memberMapper;
	private final PasswordService passwordService;

	@Override
	public String findUserPassword(Long userNo, String password) {
		
		// 입력한 비밀번호 유효성 검사
		if(password == null || password.trim().isEmpty()) {
			throw new CustomException(ErrorCode.INVALID_LOGIN_INFO);
		}
		
		// 사용자 정보 조회
		MemberDTO member = getValidMember(userNo);
		
		// 암호화된 비밀번호 비교
		String encryptedPassword = member.getUserPw();
		if(encryptedPassword == null || encryptedPassword.isBlank()) {
			throw new CustomException(ErrorCode.INVALID_LOGIN_INFO);
		}
		
		boolean isMatch = passwordService.matches(password, encryptedPassword);
		return isMatch ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다.";
	}

	
	@Override
	public void updateUserPassword(Long userNo, @Valid MypagePasswordUpdateDTO dto) {
		getValidMember(userNo);
	    String encryptedPassword = passwordService.encodePassword(dto.getNewPassword());

	    int result = memberMapper.updatePassword(userNo, encryptedPassword);
	    if (result == 0) {
	    	throw new CustomException(ErrorCode.USER_UPDATE_FAILED);
	    }
	}
	
	
	/**
	 * 사용자 확인
	 * @param userNo
	 * @return
	 */
	private MemberDTO getValidMember(Long userNo) {
	    MemberDTO member = memberMapper.findByUserNo(userNo);
	    if (member == null) {
	        throw new CustomException(ErrorCode.NOT_FOUND_USER);
	    }
	    return member;
	}

}
