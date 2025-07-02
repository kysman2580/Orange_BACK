package com.kh.dotogether.profile.model.service;


import org.springframework.web.multipart.MultipartFile;


public interface ProfileService {

	/**
	 * 등록
	 * @param profileDto
	 * @return
	 */
	String setProfile(Long userNo, MultipartFile file);
	
	/**
	 * 조회
	 * @return
	 */
	String findProfile(Long userNo);
	
	/**
	 * 수정
	 * @param userNo
	 * @return
	 */
	String updateProfile(Long userNo, MultipartFile file);
	
	/**
	 * 삭제
	 * @param userNo
	 */
	void deleteProfile(Long userNo);
	
}
