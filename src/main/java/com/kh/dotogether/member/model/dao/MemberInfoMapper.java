package com.kh.dotogether.member.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.dotogether.member.model.dto.MemberAddressDTO;

@Mapper
public interface MemberInfoMapper {
	
	/**
	 * 개인정보수정 - 연락처 조회
	 * @param userNo
	 * @return
	 */
	String findUserPhone(@Param("userNo") Long userNo);
	
	/**
	 * 개인정보수정 - 이메일 조회
	 * @param userNo
	 * @return
	 */
	String findUserEmail(@Param("userNo") Long userNo);
	
	/**
	 * 개인정보수정 - 주소 조회
	 * @param userNo
	 * @return
	 */
	MemberAddressDTO findUserAddress(@Param("userNo") Long userNo);

}
