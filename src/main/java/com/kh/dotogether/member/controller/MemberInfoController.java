package com.kh.dotogether.member.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.member.model.dto.MypagePasswordUpdateDTO;
import com.kh.dotogether.member.model.service.MemberInfoService;
import com.kh.dotogether.util.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("api/info")
@RequiredArgsConstructor
public class MemberInfoController {
	
	private final MemberInfoService memberInfoService;

	/**
	 * 비밀번호 변경 - 일치하는지 확인
	 * @param userNo
	 * @param password
	 * @return
	 */
	@PostMapping("/password-check/{userNo}")
	public ResponseEntity<ResponseData> findUserPassword(
			@PathVariable("userNo") Long userNo,
			@RequestBody Map<String, String> body){
		
		String password = body.get("userPassword");
		String message = memberInfoService.findUserPassword(userNo, password);
		
		return ResponseEntity.ok(
				ResponseData.builder()
				.code("200")
				.message(message)
				.items(Collections.emptyList())
				.build()
		);
	}
	
	@PutMapping("/password-update/{userNo}")
	public ResponseEntity<ResponseData> updateUserPassword(
			@PathVariable("userNo") Long userNo,
			@Valid @RequestBody MypagePasswordUpdateDTO dto) {
		
		memberInfoService.updateUserPassword(userNo, dto);
		
		return ResponseEntity.ok(
				ResponseData.builder()
	            .code("200")
	            .message("비밀번호가 성공적으로 변경되었습니다.")
	            .items(Collections.emptyList())
	            .build()
	    );
	}
}
