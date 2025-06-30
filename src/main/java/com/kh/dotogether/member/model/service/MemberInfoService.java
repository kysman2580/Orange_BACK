package com.kh.dotogether.member.model.service;

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

}
