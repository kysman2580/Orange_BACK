package com.kh.dotogether.section.model.service;

import com.kh.dotogether.section.model.dto.SectionDTO;


public interface SectionService {

	/**
	 * 섹션 추가
	 * @param sectionDTO
	 */
	void setSection(SectionDTO sectionDTO, String authorizationHeader);
	
	
	/**
	 * 섹션 제목(sectionTitle) 중복 확인
	 * @param sectionTitle
	 * @return true => 중복, false => 사용 가능
	 */	
	
	boolean existsByTitle(String sectionTitle, String authorizationHeader);
	
	/**
	 * isBaseSection이 'Y'인지 확인
	 * @param isBaseSection
	 * @return true => 기준섹션 존재 isBaseSection 'N'설정
	 * 		   false => 기준섹션 존재 X isBaseSection 'Y' 설정
	 */

	/**
	 * 
	 * @param sectionTitle
	 * @param userNo
	 */
	void updateSectionTitle(SectionDTO sectionDTO, String authorizationHeader);
	
	
	
	boolean existsBaseSection(Long userNo);

	

	void deleteSection(Long sectionNo, String authorizationHeader);


	
	

}
