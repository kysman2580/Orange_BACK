package com.kh.dotogether.profile.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.dotogether.profile.model.service.ProfileService;
import com.kh.dotogether.util.ResponseData;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/profile")
@RequiredArgsConstructor
@RestController
public class ProfileController {

	private final ProfileService profileService;
	
	/**
	 * 프로필 등록
	 * @param userNo
	 * @param file
	 * @return
	 */
	@PostMapping("/{userNo}")
	public ResponseEntity<ResponseData> setProfile(
			@PathVariable("userNo") Long userNo,
			@RequestParam("file") MultipartFile file) {
		
		String uploadedUrl = profileService.setProfile(userNo, file);
		return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("프로필사진 등록을 성공했습니다.")
	            .items(List.of(uploadedUrl))
	            .build()
	    ); 
	}
	
	/**
	 * 프로필 수정
	 * @param userNo
	 * @param file
	 * @return
	 */
	@PutMapping("/{userNo}")
	public ResponseEntity<ResponseData> updateProfile(
			@PathVariable("userNo") Long userNo,
			@RequestParam("file") MultipartFile file) {
		
		String uploadedUrl = profileService.updateProfile(userNo, file);
		return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("프로필사진 수정을 성공했습니다.")
	            .items(List.of(uploadedUrl))
	            .build()
	    );
	}
	
	/**
	 * 프로필 조회
	 * @param userNo
	 * @return
	 */
	@GetMapping("/{userNo}")
	public ResponseEntity<ResponseData> findProfile(@PathVariable("userNo") Long userNo) {
		String profile = profileService.findProfile(userNo);
		return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("프로필사진 조회를 성공했습니다.")
	            .items(List.of(profile))
	            .build()
	    );
	}
	
	/**
	 * 프로필 삭제
	 * @param userNo
	 * @return
	 */
	@DeleteMapping("/{userNo}")
	public ResponseEntity<ResponseData> deleteProfile(@PathVariable("userNo") Long userNo){
		profileService.deleteProfile(userNo);
		return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("프로필사진 삭제를 성공했습니다.")
	            .items(Collections.emptyList())
	            .build()
	    ); 
	}
}
