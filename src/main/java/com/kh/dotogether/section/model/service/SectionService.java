package com.kh.dotogether.section.model.service;

import java.util.List;

import com.kh.dotogether.section.model.dto.SectionDTO;


public interface SectionService {

	void setSection(SectionDTO sectionDTO);
	boolean existsByTitle(String sectionTitle);
	void updateSectionTitle(SectionDTO sectionDTO);
	boolean existsBaseSection(Long userNo);
	void deleteSection(Long sectionNo);
	List<SectionDTO> findAllSectionsWithSchedules();
	SectionDTO findSectionWithSchedules(Long sectionNo);
}





	
	

