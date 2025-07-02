package com.kh.dotogether.member.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.member.model.dto.MemberAddressDTO;
import com.kh.dotogether.member.model.dto.MemberInfoUpdateDTO;
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
	
	/**
	 * 개인정보 수정 - 연락처 조회
	 * @param userNo
	 * @return
	 */
	@GetMapping("/phone/{userNo}")
	public ResponseEntity<ResponseData> findUserPhone(@PathVariable("userNo") Long userNo) {
		String userPhone = memberInfoService.findUserPhone(userNo);
		
		return ResponseEntity.ok(
				ResponseData.builder()
				.code("200")
				.message("연락처 조회 성공")
				.items(List.of(userPhone))
				.build()
		);
	}
	
	/**
	 * 개인정보 수정 - 이메일 조회
	 * @param userNo
	 * @return
	 */
	@GetMapping("/email/{userNo}")
	public ResponseEntity<ResponseData> findUserEmail(@PathVariable("userNo") Long userNo) {
		String userEmail = memberInfoService.findUserEmail(userNo);
		return ResponseEntity.ok(
				ResponseData.builder()
				.code("200")
				.message("이메일 조회 성공")
				.items(List.of(userEmail))
				.build()
		);
	}
	
	/**
	 * 개인정보수정 - 주소 조회
	 * @param userNo
	 * @return
	 */
	@GetMapping("/address/{userNo}")
	public ResponseEntity<ResponseData> findUserAddress(@PathVariable("userNo") Long userNo) {
		MemberAddressDTO userAddress = memberInfoService.findUserAddress(userNo);
		return ResponseEntity.ok(
				ResponseData.builder()
				.code("200")
				.message("주소 조회 성공")
				.items(List.of(userAddress))
				.build()
		);
	}

	/**
	 * 개인정보수정
	 * @param userNo
	 * @param dto
	 * @return
	 */
	@PutMapping("/{userNo}")
	public ResponseEntity<ResponseData> updateUserInfo(
			@PathVariable("userNo") Long userNo, @RequestBody MemberInfoUpdateDTO dto) {
		memberInfoService.updateUserInfo(userNo, dto);
		return ResponseEntity.ok(
		        ResponseData.builder()
		            .code("200")
		            .message("회원 정보 수정 완료")
		            .items(Collections.emptyList())
		            .build()
	    );
	}
	
}
