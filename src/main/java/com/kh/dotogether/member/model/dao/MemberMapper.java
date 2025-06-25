package com.kh.dotogether.member.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.dotogether.member.model.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	
	 /**
     * 회원가입
     * @param memberDTO
     */
    void signUp(MemberDTO memberDTO);

    /**
     * 아이디(userId) 중복 체크
     * @param userId
     * @return 1 이상이면 중복
     */
    int existsByUserId(@Param("userId") String userId);

    /**
     * 이메일 중복 체크
     * @param userEmail (암호화된 값)
     * @return 1 이상이면 중복
     */
    int existsByEmail(@Param("userEmail") String userEmail);

    /**
     * 연락처 중복 체크
     * @param userPhone (암호화된 값)
     * @return 1 이상이면 중복
     */
    int existsByPhone(@Param("userPhone") String userPhone);

    /**
     * 회원 정보 조회 (userNo 기준)
     * @param userNo
     * @return MemberDTO
     */
    MemberDTO findByUserNo(@Param("userNo") Long userNo);
    
    /**
     * 회원 정보 조회 (userId 기준)
     * @param userId
     * @return MemberDTO
     */
    MemberDTO findByUserId(@Param("userId") String userId);

    /**
     * 회원 탈퇴 (userStatus = 'N' 으로 변경)
     * @param userNo
     * @return 업데이트 결과
     */
    int deleteUser(@Param("userNo") Long userNo);

    /**
     * 비밀번호 업데이트
     * @param userNo
     * @param userPw (암호화된 비밀번호)
     * @return 업데이트 결과
     */
    int updatePassword(@Param("userNo") Long userNo, @Param("userPw") String userPw);
	
}
