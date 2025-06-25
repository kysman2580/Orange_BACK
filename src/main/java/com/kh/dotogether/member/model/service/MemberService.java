package com.kh.dotogether.member.model.service;

import com.kh.dotogether.member.model.dto.MemberDTO;

public interface MemberService {

    /**
     * 회원가입
     * @param memberDTO
     */
    void signUp(MemberDTO memberDTO);

    /**
     * 아이디(userId) 중복 확인
     * @param userId
     * @return true → 중복, false → 사용 가능
     */
    boolean isUserIdDuplicated(String userId);

    /**
     * 이메일 중복 확인
     * @param email
     * @return true → 중복, false → 사용 가능
     */
    boolean isEmailDuplicated(String email);

    /**
     * 연락처 중복 확인
     * @param phone
     * @return true → 중복, false → 사용 가능
     */
    boolean isPhoneDuplicated(String phone);

    /**
     * 회원 탈퇴
     * @param userNo
     * @return true → 성공, false → 실패
     */
    boolean deleteUser(Long userNo, String authorizationHeader);

    /**
     * 회원 정보 조회 (userNo 기준)
     * @param userNo
     * @return MemberDTO
     */
    MemberDTO findByUserNo(Long userNo);

    /**
     * 비밀번호 재설정
     * @param userNo
     * @param email
     * @param newPassword
     */
    void resetPassword(Long userNo, String email, String newPassword);
}
