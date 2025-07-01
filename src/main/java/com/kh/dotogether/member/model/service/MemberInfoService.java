package com.kh.dotogether.member.model.service;

import com.kh.dotogether.member.model.dto.MemberAddressDTO;
import com.kh.dotogether.member.model.dto.MemberInfoUpdateDTO;
import com.kh.dotogether.member.model.dto.MypagePasswordUpdateDTO;

import jakarta.validation.Valid;

public interface MemberInfoService {

	/**
	 * 입력한 비밀번호와 일치하는지 확인
	 */
	String findUserPassword(Long userNo, String password);

	/**
	 * 비밀번호 변경
	 * @param userNo
	 * @param dto
	 */
	void updateUserPassword(Long userNo, @Valid MypagePasswordUpdateDTO dto);

	/**
	 * 연락처 조회
	 * @param userNo
	 */
	String findUserPhone(Long userNo);

	/**
	 * 이메일 조회
	 * @param userNo
	 * @return
	 */
	String findUserEmail(Long userNo);

	/**
	 * 주소 조회
	 * @param userNo
	 * @return
	 */
	MemberAddressDTO findUserAddress(Long userNo);

	/**
	 * 개인정보 수정
	 * @param userNo
	 * @param dto
	 */
	void updateUserInfo(Long userNo, MemberInfoUpdateDTO dto);

	/**
	 * 연락처 중복 검사
	 * @param phone
	 * @param userNo
	 * @return
	 */
	boolean isPhoneDuplicated(String phone, Long userNo);
	
	/**
	 * 이메일 중복 검사
	 * @param email
	 * @param userNo
	 * @return
	 */
	boolean isEmailDuplicated(String email, Long userNo);
	

}
