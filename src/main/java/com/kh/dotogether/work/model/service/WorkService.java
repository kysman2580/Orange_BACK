package com.kh.dotogether.work.model.service;

import java.util.List;
import java.util.Map;

import com.kh.dotogether.work.model.dto.WorkDTO;

public interface WorkService {

	List<WorkDTO> findWorkList(String teamId, String status);
	
	WorkDTO addWork(WorkDTO request);
	
	WorkDTO updateWorkStatus(WorkDTO request);
	
	WorkDTO updateWorkDetail(WorkDTO request);
	
	WorkDTO deleteWorkByWorkNo(WorkDTO request);
	
}
