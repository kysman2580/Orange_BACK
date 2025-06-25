package com.kh.dotogether.token.model.service;

import java.util.Map;

public interface TokenService {
	
	/**
	 * 토큰 생성 (AccessToken + RefreshToken)
	 * @param userId 사용자 ID (문자열)
	 * @param role 사용자 권한 (ROLE_USER 등)
	 * @return 액세스 토큰과 리프레시 토큰을 담은 Map
	 */
	Map<String, String> generateToken(String userId, String role);
	
	/**
	 * 리프레시 토큰으로 새로운 토큰 발급
	 * @param refreshToken
	 * @return 액세스 토큰 + 리프레시 토큰
	 */
	Map<String, String> refreshToken(String refreshToken);
	
	/**
	 * 특정 사용자 토큰 삭제
	 * @param userNo 사용자 No
	 */
	void deleteUserToken(Long userNo);
}
