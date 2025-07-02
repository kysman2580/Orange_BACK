package com.kh.dotogether.section.controller;

import java.util.Collections;

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

import com.kh.dotogether.section.model.dto.SectionDTO;
import com.kh.dotogether.section.model.service.SectionService;
import com.kh.dotogether.util.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/section")
@RequiredArgsConstructor
public class SectionController {

	private final SectionService sectionService;

	@PostMapping
	public ResponseEntity<ResponseData> setSection(
			@RequestBody @Valid SectionDTO sectionDTO,
		    @RequestHeader("Authorization") String authorizationHeader) {

		sectionService.setSection(sectionDTO, authorizationHeader);

		return ResponseEntity.ok(ResponseData.builder()
							 .code("200")
							 .message("섹션 추가에 성공했습니다.")
							 .items(Collections.emptyList())
							 .build());
	}

	@GetMapping("/check-title")
	public ResponseEntity<ResponseData> checkSectionTitle(
			@RequestParam("sectionTitle") String sectionTitle,
			@RequestHeader("Authorization") String authorizationHeader) {

		boolean exists = sectionService.existsByTitle(sectionTitle, authorizationHeader);

		String message = exists ? "중복된 제목이 존재합니다." : "사용 가능한 제목입니다.";
		String code = exists ? "409" : "200";

		return ResponseEntity
				.ok(ResponseData.builder()
				.code(code)
				.message(message)
				.items(Collections.emptyList())
				.build());
	}

	@PostMapping("/title-update")
	public ResponseEntity<ResponseData> updateSectionTitle(
			@RequestBody @Valid SectionDTO sectionDTO,
			@RequestHeader("Authorization") String authorizationHeader) {

		sectionService.updateSectionTitle(sectionDTO, authorizationHeader);

		return ResponseEntity
				.ok(ResponseData.builder()
				.code("200")
				.message("제목 수정 완료")
				.items(Collections.emptyList())
				.build());
	}
	
	@DeleteMapping("/{sectionNo}")
	public ResponseEntity<ResponseData> deleteSection(
	        @PathVariable("sectionNo") Long sectionNo,
	        @RequestHeader("Authorization") String authorizationHeader) {

	    sectionService.deleteSection(sectionNo, authorizationHeader);

	    return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("섹션이 성공적으로 삭제되었습니다.")
	            .items(Collections.emptyList())
	            .build());
	}
	
	
	
	
}
