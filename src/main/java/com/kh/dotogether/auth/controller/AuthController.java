package com.kh.dotogether.auth.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.auth.model.dto.LoginDTO;
import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.exception.InvalidTokenException;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.member.model.service.MemberService;
import com.kh.dotogether.token.model.service.TokenService;
import com.kh.dotogether.util.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
	
	private final AuthService authService;
	private final TokenService tokenService;
	
	/**
	 * 로그인
	 * @param loginDTO
	 * @return
	 */
	@PostMapping("/tokens")
	public ResponseEntity<ResponseData> login(@RequestBody @Valid LoginDTO loginDTO) {
		log.info("로그인 요청: {}", loginDTO.getUserId());
		Map<String, String> tokenMap = authService.login(loginDTO);
		
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("로그인에 성공했습니다.")
				.items(List.of(tokenMap))
				.build()
		);
	}
	
	/**
	 * 리프레시 토큰으로 재발급
	 * @param tokenMap
	 * @return
	 */
	@PostMapping("/refresh")
	public ResponseEntity<ResponseData> refresh(@RequestBody Map<String, String> tokenMap) {
		String refreshToken = tokenMap.get("refreshToken");
		
		if (refreshToken == null || refreshToken.isEmpty()) {
	        return ResponseEntity.badRequest().body(
	            ResponseData.builder()
	                .code("400")
	                .message("리프레시 토큰이 없습니다.")
	                .items(Collections.emptyList())
	                .build()
	        );
	    }

	    try {
	        Map<String, String> newTokens = tokenService.refreshToken(refreshToken);

	        return ResponseEntity.ok(
	            ResponseData.builder()
	                .code("200")
	                .message("토큰이 갱신되었습니다.")
	                .items(List.of(newTokens))
	                .build()
	        );

	    } catch (CustomException e) {
	        log.error("토큰 갱신 실패: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                ResponseData.builder()
	                        .code("401")
	                        .message(e.getMessage())
	                        .items(Collections.emptyList())
	                        .build()
	        );
	    } catch (Exception e) {
	    	log.error("토큰 갱신 실패: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	                ResponseData.builder()
	                        .code("500")
	                        .message("서버 오류가 발생했습니다.")
	                        .items(Collections.emptyList())
	                        .build()
	        );
	    }
	}
	
	
	/**
	 * 로그아웃
	 * @param authorizationHeader
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<ResponseData> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
		authService.logout(authorizationHeader);
		
		return ResponseEntity.ok(
		        ResponseData.builder()
		            .code("200")
		            .message("로그아웃이 완료되었습니다.")
		            .items(Collections.emptyList())
		            .build()
	    );
	}

}
