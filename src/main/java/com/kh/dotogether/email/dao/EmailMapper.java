package com.kh.dotogether.email.dao;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.dotogether.email.dto.EmailVerificationDTO;

@Mapper
public interface EmailMapper {
	
	void insertVerification(
	    @Param("userNo") Long userNo,
	    @Param("email") String email,
	    @Param("code") String code,
	    @Param("expireAt") LocalDateTime expireAt
	);
	
	EmailVerificationDTO findVerificationByEmail(@Param("email") String email);
	
	int deleteVerificationByEmail(@Param("email") String email);
	
	boolean existsByUserIdAndEmail(
			@Param("userId") String userId,
			@Param("userEmail") String userEmail);
}
