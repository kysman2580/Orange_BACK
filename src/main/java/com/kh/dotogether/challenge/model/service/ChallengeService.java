package com.kh.dotogether.challenge.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.dotogether.challenge.model.dto.ChallengeDTO;

public interface ChallengeService {
	
	void save(ChallengeDTO challenge, MultipartFile file);
	
	List<ChallengeDTO> findAll(int pageNo);
	
	ChallengeDTO findById(Long challengeNo);
	
	ChallengeDTO update(ChallengeDTO challenge, MultipartFile file);
	
	void deleteById(Long challengeNo);
	
}
