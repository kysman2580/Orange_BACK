package com.kh.dotogether.section.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.dotogether.section.model.dto.SectionDTO;

@Mapper
public interface SectionMapper {

	int existsByTitle(@Param("sectionTitle") String sectionTitle, 
					  @Param("userNo") Long userNo);
	
	int countBaseSection(Long userNo);

	void insertSection(SectionDTO sectionDTO);

	void updateSectionTitle(@Param("sectionTitle") String sectionTitle, 
							@Param("userNo") Long userNo, 
							@Param("sectionNo") Long sectionNo);

	int findBySection(Long userNo);

	SectionDTO findLastestSection(Long sectionNo);

	void updateBaseSection(SectionDTO oldSection);

	void deleteSection(Long sectionNo);
	
	

}
