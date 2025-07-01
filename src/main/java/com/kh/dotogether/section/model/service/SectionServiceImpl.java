package com.kh.dotogether.section.model.service;

import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.util.JWTUtil;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.member.model.dto.MemberDTO;
import com.kh.dotogether.member.model.service.MemberService;
import com.kh.dotogether.schedule.model.dto.ScheduleDTO;
import com.kh.dotogether.section.model.dao.SectionMapper;
import com.kh.dotogether.section.model.dto.SectionDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

	private final SectionMapper sectionMapper;
	private final MemberService memberService;
	private final JWTUtil jwtUtil;

	@Override
	public void setSection(SectionDTO sectionDTO, String authorizationHeader) {
		Long userNo = extractUserNo(authorizationHeader);

		String title = sectionDTO.getSectionTitle();

		if (title.isBlank()) {
			throw new CustomException(ErrorCode.EMPTY_TITLE);
		}

		if (existsByTitle(title, authorizationHeader)) {
			throw new CustomException(ErrorCode.DUPLICATE_SECTION_TITLE);
		}

		sectionDTO.setUserNo(userNo);

		if (existsBaseSection(userNo)) {
			sectionDTO.setIsBaseSection("N");
		} else {
			sectionDTO.setIsBaseSection("Y");
		}

		sectionMapper.insertSection(sectionDTO);
	}

	@Override
	public boolean existsByTitle(String sectionTitle, String authorizationHeader) {
		Long userNo = extractUserNo(authorizationHeader);
		return sectionMapper.existsByTitle(sectionTitle, userNo) > 0;
	}

	@Override
	public void updateSectionTitle(SectionDTO sectionDTO, String authorizationHeader) {
		Long userNo = extractUserNo(authorizationHeader);
		String title = sectionDTO.getSectionTitle();
		Long sectionNo = sectionDTO.getSectionNo();

		if (title.isBlank()) {
			throw new CustomException(ErrorCode.EMPTY_TITLE);
		}
		if (existsByTitle(title, authorizationHeader)) {
			throw new CustomException(ErrorCode.DUPLICATE_SECTION_TITLE);
		}

		sectionMapper.updateSectionTitle(title, userNo, sectionNo);
	}

	@Override
	public boolean existsBaseSection(Long userNo) {
		return sectionMapper.countBaseSection(userNo) > 0;
	}
	
	
	
	@Override
	public void deleteSection(Long sectionNo, String authorizationHeader) {
		Long userNo = extractUserNo(authorizationHeader);
		
		int sectionCount = sectionMapper.findBySection(userNo);
		log.info("조회된 섹션 갯수 : {}", sectionCount);
		
		if(sectionCount == 0) {	// 섹션이 존재하지 않을 때
			throw new CustomException(ErrorCode.SECTION_NOT_FOUND);
		}
		log.info("조회된 섹션 갯수 : {}", sectionCount);
		
		if(sectionCount == 1) {	//섹션이 하나 남았을 때
			throw new CustomException(ErrorCode.CANNOT_DELETE_LAST_SECTION);
		}
		log.info("조회된 섹션 갯수 : {}", sectionCount);
		
		reassignBaseAndDelete(sectionNo, userNo);
		
	}
	
	
	// 스케줄 이동 메서드
	private void moveSchedulesToSection(ScheduleDTO scheduleDTO) {
		
		
	}
	
	
	/**
	 * 기준 섹션으로 업데이트 및 기존 섹션 삭제
	 * @param sectionNo
	 * @param userNo
	 */
	private void reassignBaseAndDelete(Long sectionNo, Long userNo) {
		SectionDTO newBaseSection = findLastestSection(sectionNo);
		newBaseSection.setIsBaseSection("Y");
		newBaseSection.setUserNo(userNo);
		
		sectionMapper.updateBaseSection(newBaseSection);
		log.info("기준 섹션으로 승격 : {}", newBaseSection);
		
		sectionMapper.deleteSection(sectionNo);
	}
	
	/**
	 * 해당 섹션 다음으로 생성된 섹션
	 * @param sectionNo
	 * @return 
	 */
	private SectionDTO findLastestSection(Long sectionNo) {
		SectionDTO newBaseSection = sectionMapper.findLastestSection(sectionNo);
		return newBaseSection;
	}
	
	
	private Long extractUserNo(String authorizationHeader) {
		String token = authorizationHeader.replace("Bearer ", "");
		String userId = jwtUtil.getUserIdFromToken(token);
		MemberDTO member = memberService.findByUserId(userId);
		return member.getUserNo();
	}

	
}
