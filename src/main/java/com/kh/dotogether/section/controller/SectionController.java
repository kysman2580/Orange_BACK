package com.kh.dotogether.section.controller;

import java.util.Collections;
import java.util.List;

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
	public ResponseEntity<ResponseData> setSection(@RequestBody @Valid SectionDTO sectionDTO) {
		sectionService.setSection(sectionDTO);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("섹션 추가에 성공했습니다.")
				.items(Collections.emptyList())
				.build());
	}

	@GetMapping("/check-title")
	public ResponseEntity<ResponseData> checkSectionTitle(@RequestParam("sectionTitle") String sectionTitle) {
		boolean exists = sectionService.existsByTitle(sectionTitle);
		String message = exists ? "중복된 제목이 존재합니다." : "사용 가능한 제목입니다.";
		String code = exists ? "409" : "200";
		return ResponseEntity.ok(ResponseData.builder()
				.code(code)
				.message(message)
				.items(Collections.emptyList())
				.build());
	}

	@GetMapping("/dashboard")
	public ResponseEntity<ResponseData> findAllSectionsWithSchedules() {
		List<SectionDTO> sections = sectionService.findAllSectionsWithSchedules();
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("섹션 및 일정 전체 조회 성공")
				.items(sections)
				.build());
	}

	@GetMapping("/{sectionNo}")
	public ResponseEntity<ResponseData> getSectionWithSchedules(@PathVariable("sectionNo") Long sectionNo) {
		SectionDTO section = sectionService.findSectionWithSchedules(sectionNo);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("섹션 및 스케줄 조회 완료")
				.items(List.of(section))
				.build());
	}

	@PostMapping("/title-update")
	public ResponseEntity<ResponseData> updateSectionTitle(@RequestBody @Valid SectionDTO sectionDTO) {
		sectionService.updateSectionTitle(sectionDTO);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("제목 수정 완료")
				.items(Collections.emptyList())
				.build());
	}

	@DeleteMapping("/{sectionNo}")
	public ResponseEntity<ResponseData> deleteSection(@PathVariable("sectionNo") Long sectionNo) {
		sectionService.deleteSection(sectionNo);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("섹션이 성공적으로 삭제되었습니다.")
				.items(Collections.emptyList())
				.build());
	}
}

