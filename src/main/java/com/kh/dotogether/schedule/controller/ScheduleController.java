package com.kh.dotogether.schedule.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.schedule.model.dto.ScheduleDTO;
import com.kh.dotogether.schedule.model.service.ScheduleService;
import com.kh.dotogether.section.model.dto.SectionDTO;
import com.kh.dotogether.util.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	
	@PostMapping
	public ResponseEntity<ResponseData> setSchedule(
			@RequestBody @Valid ScheduleDTO scheduleDTO) {

		scheduleService.setSchedule(scheduleDTO);

		return ResponseEntity.ok(ResponseData.builder()
							 .code("200")
							 .message("일정 추가에 성공했습니다.")
							 .items(Collections.emptyList())
							 .build());
	}
	
	@GetMapping("/{scheduleNo}")
	public ResponseEntity<ResponseData> findSchedule(@PathVariable("scheduleNo") Long scheduleNo) {
	    ScheduleDTO schedule = scheduleService.findScheduleById(scheduleNo);
	    return ResponseEntity.ok(ResponseData.builder()
	            .code("200")
	            .message("일정 상세 조회 성공")
	            .items(Collections.singletonList(schedule))  // 단일 일정도 리스트로 감싸기
	            .build());
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<ResponseData> updateSchedule(@RequestBody @Valid ScheduleDTO scheduleDTO) {
		scheduleService.updateSchedule(scheduleDTO);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("일정 수정 완료")
				.items(Collections.emptyList())
				.build());
	}
	
	@DeleteMapping("/{scheduleNo}")
	public ResponseEntity<ResponseData> deleteSection(@PathVariable("scheduleNo") Long scheduleNo) {
		scheduleService.deleteSchedule(scheduleNo);
		return ResponseEntity.ok(ResponseData.builder()
				.code("200")
				.message("일정이 성공적으로 삭제되었습니다.")
				.items(Collections.emptyList())
				.build());
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
