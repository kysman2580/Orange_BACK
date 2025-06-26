package com.kh.dotogether.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.common.ResponseData;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.member.model.service.MemberService;

import jakarta.validation.Valid;
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
	public ResponseEntity<ResponseData<Object>> signUp(@RequestBody @Valid MemberDTO memberDTO) {
		memberService.signUp(memberDTO);
		log.info("회원가입 성공: {}", memberDTO.getUserId());
		
		return ResponseEntity.ok(ResponseData.<Object>builder()
		            .code("200")
		            .message("회원가입에 성공했습니다.")
		            .items(null)
		            .build()
		    );
	}
	
	/**
	 * 회원 탈퇴
	 * @param userNo
	 * @return
	 */
	@DeleteMapping("/{userNo}")
	public ResponseEntity<ResponseData<Object>> deleteUser(
					@PathVariable("userNo") Long userNo,
					@RequestHeader(value = "Authorization") String authorizationHeader) {
		
		memberService.deleteUser(userNo, authorizationHeader);
		
		return ResponseEntity.ok(ResponseData.<Object>builder()
		            .code("200")
		            .message("회원탈퇴가 정상적으로 처리되었습니다.")
		            .items(null)
		            .build()
		    );
	}
	
	/**
     * 아이디 중복 확인
     */
    @GetMapping("/check-id/{userId}")
    public ResponseEntity<ResponseData<Object>> checkId(@PathVariable("userId") String userId) {
        boolean duplicated = memberService.isUserIdDuplicated(userId);

        String message = duplicated ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.";

        return ResponseEntity.ok(ResponseData.<Object>builder()
                .code(duplicated ? "409" : "200")
                .message(message)
                .items(null)
                .build());
    }

    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ResponseData<Object>> checkEmail(@PathVariable("email") String email) {
        boolean duplicated = memberService.isEmailDuplicated(email);

        String message = duplicated ? "이미 사용중인 이메일입니다." : "사용 가능한 이메일입니다.";

        return ResponseEntity.ok(ResponseData.<Object>builder()
                .code(duplicated ? "409" : "200")
                .message(message)
                .items(null)
                .build());
    }

    /**
     * 연락처 중복 확인
     */
    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<ResponseData<Object>> checkPhone(@PathVariable("phone") String phone) {
        boolean duplicated = memberService.isPhoneDuplicated(phone);

        String message = duplicated ? "이미 사용중인 연락처입니다." : "사용 가능한 연락처입니다.";

        return ResponseEntity.ok(ResponseData.<Object>builder()
                .code(duplicated ? "409" : "200")
                .message(message)
                .items(null)
                .build());
    }
	
}
