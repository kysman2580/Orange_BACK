package com.kh.dotogether.member.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.member.model.dto.UserIdResponseDTO;
import com.kh.dotogether.member.model.service.MemberService;
import com.kh.dotogether.util.ResponseData;
import com.kh.dotogether.util.ResponseUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {

	private final MemberService memberService;
	
	/**
	 * 회원가입 API
	 * @param memberDTO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ResponseData> signUp(@RequestBody @Valid MemberDTO memberDTO) {
		memberService.signUp(memberDTO);
		log.info("회원가입 성공: {}", memberDTO.getUserId());
		
		return ResponseEntity.ok(ResponseData.builder()
		            .code("200")
		            .message("회원가입에 성공했습니다.")
		            .items(Collections.emptyList())
		            .build()
		    );
	}
	
	/**
	 * 회원 탈퇴
	 * @param userNo
	 * @return
	 */
	@DeleteMapping("/{userNo}")
	public ResponseEntity<ResponseData> deleteUser(
					@PathVariable("userNo") Long userNo,
					@RequestHeader(value = "Authorization") String authorizationHeader) {
		
		memberService.deleteUser(userNo, authorizationHeader);
		
		return ResponseEntity.ok(ResponseData.builder()
		            .code("200")
		            .message("회원탈퇴가 정상적으로 처리되었습니다.")
		            .items(Collections.emptyList())
		            .build()
		    );
	}
	
	/**
     * 아이디 중복 확인
     */
    @GetMapping("/check-id/{userId}")
    public ResponseEntity<ResponseData> checkId(@PathVariable("userId") String userId) {
        boolean duplicated = memberService.isUserIdDuplicated(userId);
        return ResponseUtil.buildDuplicationResponse(duplicated, "아이디");
    }

    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ResponseData> checkEmail(@PathVariable("email") String email) {
        boolean duplicated = memberService.isEmailDuplicated(email);
        return ResponseUtil.buildDuplicationResponse(duplicated, "이메일");
    }

    /**
     * 연락처 중복 확인
     */
    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<ResponseData> checkPhone(@PathVariable("phone") String phone) {
        boolean duplicated = memberService.isPhoneDuplicated(phone);
        return ResponseUtil.buildDuplicationResponse(duplicated, "연락처");
    }
    
    
    /**
     * 아이디 찾기
     * @param userName
     * @param userEmail
     * @return
     */
	@GetMapping("/find-id/{userName}")
	public ResponseEntity<ResponseData> findUserId(
			@PathVariable("userName") String userName,
			@RequestParam(name="userEmail") @Email(message="이메일 형식이 올바르지 않습니다.") String userEmail) {
		
		UserIdResponseDTO result = memberService.findUserId(userName, userEmail);
		
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("아이디 조회 성공")
                .items(List.of(result))
                .build());
	}
	
	/**
	 * 비밀번호 찾기(1단계 - 아이디 조회)
	 * @param userId
	 * @return
	 */
	@GetMapping("/find-pw/{userId}")
	public ResponseEntity<ResponseData> findPasswordStep1(@PathVariable("userId") String userId){
		log.info("비밀번호 찾기 1단계 - 아이디 확인 요청 : {}", userId);
		memberService.findByUserId(userId);
		
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("아이디 확인 성공")
				.items(List.of(Map.of("userId", userId)))
                .build());
	}
	
	
//	@PostMapping("/send-code")
//	public ResponseEntity<ResponseData<Object>> senderVerificationCode(@Valid @RequestBody EmailVerificationDTO dto){
//		return null;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
