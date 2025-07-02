package com.kh.dotogether.work.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.util.ResponseData;
import com.kh.dotogether.work.model.dto.WorkDTO;
import com.kh.dotogether.work.model.service.WorkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/works")
public class WorkController {

	private final WorkService workService;
	
	@GetMapping
	public ResponseEntity<ResponseData> findWorkList(@RequestParam(name="teamId") String teamId,
													 @RequestParam(name="status") String status){
		
	 	List<WorkDTO> workList = workService.findWorkList(teamId, status);
		
	 	ResponseData responseData = ResponseData.builder()
				.code("S313")
				.message("업무가 조회 되었습니다.")
				.items(workList)
				.build();	

	 	return ResponseEntity.ok(responseData);
	 	
	}
}
