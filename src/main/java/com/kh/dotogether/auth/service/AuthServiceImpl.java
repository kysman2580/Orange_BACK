package com.kh.dotogether.auth.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.model.dto.LoginDTO;
import com.kh.dotogether.auth.model.vo.CustomUserDetails;
import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.exception.CustomAuthenticationException;
import com.kh.dotogether.member.model.dao.MemberMapper;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.password.service.PasswordService;
import com.kh.dotogether.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final TokenService tokenService;
	private final JWTUtil jwtUtil;
	private final MemberMapper memberMapper;
	private final PasswordService passwordService;
	
	@Override
	public Map<String, String> login(LoginDTO loginDTO) {
		
		// 사용자 조회
	    MemberDTO user = memberMapper.findByUserId(loginDTO.getUserId());
	    if (user == null) {
	        throw new CustomAuthenticationException("존재하지 않는 회원입니다.");
	    }
	    
	    // 탈퇴 회원 확인
	    if ("N".equals(user.getUserStatus())) {
	    	throw new CustomAuthenticationException("탈퇴한 회원입니다.");
	    }

	    // 비밀번호 검증
	    if (!passwordService.matches(loginDTO.getUserPw(), user.getUserPw())) {
	        throw new CustomAuthenticationException("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
	    }
	    
		log.info("로그인 성공!");
		log.info("인증된 사용자 정보: {}", user);
		
		// 토큰 발급
		Map<String, String> loginResponse = tokenService.generateToken(
				user.getUserId(), user.getUserRole()
		);
		
		// 사용자 정보 추가
		loginResponse.put("userNo", String.valueOf(user.getUserNo()));
		loginResponse.put("userId", user.getUserId());
		loginResponse.put("userName", user.getUserName());
		loginResponse.put("userRole", user.getUserRole());
		
		return loginResponse;
	}
	
	@Override
	public CustomUserDetails getUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
			return (CustomUserDetails) auth.getPrincipal();
		}
		return null;
	}

	@Override
	public void logout(String authorizationHeader) {
		try {
			if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				throw new IllegalArgumentException("유효한 인증 정보가 없습니다.");
			}
			
			String token = authorizationHeader.substring(7); // "Bearer " 제거
			String userId = jwtUtil.getUserIdFromToken(token); // userId 추출
			
			// userId 기준으로 member 조회 + userNo 가져옴
			MemberDTO member = memberMapper.findByUserId(userId);
			if(member == null) {
				throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
			}
			Long userNo = member.getUserNo();
			tokenService.deleteUserToken(userNo);
			log.info("로그아웃 처리 완료: userNo = {}, userId = {}", userNo, userId);
		} catch(Exception e) {
			// 토큰이 만료됐으면 세션도 끝났다고 보면 되므로 별도 조치 없이 로그아웃 처리
			log.warn("accessToken 파싱 실패 또는 만료됨, 로그아웃 강제 처리: {}", e.getMessage());
		}
	}

}
