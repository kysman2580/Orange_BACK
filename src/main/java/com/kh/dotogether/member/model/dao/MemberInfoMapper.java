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

	/**
	 * 개인정보수정
	 * @param userNo
	 * @param encPhone
	 * @param encEmail
	 * @param userAddress1
	 * @param userAddress2
	 * @return
	 */
	int updateUserInfo(
		    @Param("userNo") Long userNo,
		    @Param("userPhone") String encPhone,
		    @Param("userEmail") String encEmail,
		    @Param("userAddress1") String userAddress1,
		    @Param("userAddress2") String userAddress2
		);

	int existsByPhone(@Param("userPhone") String encPhone, @Param("userNo") Long userNo);
	int existsByEmail(@Param("userEmail") String encEmail, @Param("userNo") Long userNo);

}
