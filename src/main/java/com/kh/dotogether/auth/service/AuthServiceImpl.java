package com.kh.dotogether.auth.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.model.dto.LoginDTO;
import com.kh.dotogether.auth.model.vo.CustomUserDetails;
import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
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
	private final AuthenticationManager authenticationManager;
	
	@Override
	public Map<String, String> login(LoginDTO loginDTO) {
		
		// UsernamePasswordAuthenticationToken 생성
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(loginDTO.getUserId(), loginDTO.getUserPw());
		
		// AuthenticationManager를 통해 인증 처리 → UserDetailsServiceImpl 호출됨
		Authentication authentication = authenticationManager.authenticate(authToken);
		
		// 인증 성공 → 인증된 사용자 정보 꺼내기
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    
		log.info("로그인 성공 - 사용자 ID: {}", userDetails.getUserId());
		
		// 토큰 발급
		Map<String, String> loginResponse = tokenService.generateToken(
				userDetails.getUserId(), userDetails.getRole()
		);
		
		// 사용자 정보 추가
		loginResponse.put("userNo", String.valueOf(userDetails.getUserNo()));
		loginResponse.put("userId", userDetails.getUserId());
		loginResponse.put("userName", userDetails.getUserName());
		loginResponse.put("userRole", userDetails.getRole());
		
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
				throw new CustomException(ErrorCode.INVALID_AUTH_INFO);
			}
			
			String token = authorizationHeader.substring(7); // "Bearer " 제거
			String userId = jwtUtil.getUserIdFromToken(token); // userId 추출
			
			// userId 기준으로 member 조회 + userNo 가져옴
			MemberDTO member = memberMapper.findByUserId(userId);
			if(member == null) {
				throw new CustomException(ErrorCode.NOT_FOUND_USER);
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
