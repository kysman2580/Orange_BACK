package com.kh.dotogether.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kh.dotogether.auth.model.vo.CustomUserDetails;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;

public class SecurityUtil {
	// userNo 반환
	public static Long getCurrentUserNo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			throw new CustomException(ErrorCode.LOGIN_REQUIRED);
		}
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		System.out.println("현재 로그인한 사용자 번호 : " + user.getUserNo());
		return user.getUserNo();
	}
	
	// userId 반환
	public static String getCurrentUserId() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
	        throw new CustomException(ErrorCode.LOGIN_REQUIRED);
	    }
	    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
	    System.out.println("현재 로그인한 사용자 아이디 : " + user.getUserId());
	    return user.getUserId();
	}

}
