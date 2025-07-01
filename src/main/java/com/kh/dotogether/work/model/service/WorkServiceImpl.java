package com.kh.dotogether.work.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.vo.Team;
import com.kh.dotogether.team.util.TeamValidator;
import com.kh.dotogether.work.model.dao.WorkMapper;
import com.kh.dotogether.work.model.dto.WorkDTO;
import com.kh.dotogether.work.model.vo.Work;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {
	
	private final TeamValidator teamValidator;
	private final AuthService authService;
	private final WorkMapper workMapper;
	private final TeamMapper teamMapper;

	@Override
	public List<WorkDTO> findWorkList(String teamId, String status) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Team team = Team.builder()
						.userNo(userNo)
						.teamId(teamId)
						.build();
		
		teamValidator.isTeamMember(team);
				
		
		Work work = Work.builder().teamId(teamId).status(status).build();
		
		List<WorkDTO> workList = workMapper.findWorkList(work);
		
		return workList;
	}

	@Override
	public WorkDTO addWork(WorkDTO request) {
		
		System.out.println(">>>>>>>>>>>>>>>>request>>>>>>>>>>>>>" + request);
		
		String workId = UUID.randomUUID().toString();
		
		WorkDTO responseWork = new WorkDTO();
		responseWork.setRequestUserNo(request.getRequestUserNo());
		
		System.out.println(">>>>>>>>>>>>>>>>request>>>>>>>>>>>>>" + request);
		
		String isTeamMember = teamValidator.isTeamMember(request.getTeamId(), request.getRequestUserNo());
		
		if(isTeamMember != null) {
			responseWork.setType(isTeamMember);
			return responseWork;
		}
		
		
		Work work = Work.builder()
						.teamId(request.getTeamId())
						.workId(workId)
						.assigneeNo(request.getRequestUserNo())
						.build();
		
		System.out.println(">>>>>>>>>>>>>>work>>>>>>>>>>>>>>>" + work);
		
		int addWork = workMapper.addWork(work);
		
		if(addWork == 0) {
			responseWork.setType("업무 추가에 실패했습니다.");
			return responseWork;
		}
		
		responseWork = workMapper.findWorkByWorkId(workId);
		
		responseWork.setType("add");
		
		return responseWork;
	}

	@Override
	public WorkDTO updateWorkStatus(WorkDTO request) {
		
		WorkDTO responseWork = new WorkDTO();
		responseWork.setRequestUserNo(request.getRequestUserNo());
		
		String isTeamMember = teamValidator.isTeamMember(request.getTeamId(), request.getRequestUserNo());
				
		if(isTeamMember != null) {
			responseWork.setType(isTeamMember);
			return responseWork;
		}
		
		Work work = Work.builder()
						.workId(request.getWorkId())
						.status(request.getStatus())
						.build();
		
		int updateWorkStatus = workMapper.updateWorkStatus(work);
		
		if(updateWorkStatus == 0) {
			responseWork.setType("업무 상태 수정에 실패했습니다.");
			return responseWork;
		}
		
		responseWork = workMapper.findWorkByWorkId(request.getWorkId());
		
		responseWork.setPrevStatus(request.getPrevStatus());
		
		responseWork.setType("statusUpdate");
		
		return responseWork;
	}

	@Override
	public WorkDTO updateWorkDetail(WorkDTO request) {

		WorkDTO responseWork = new WorkDTO();
		responseWork.setRequestUserNo(request.getRequestUserNo());
		
		String isTeamMember = teamValidator.isTeamMember(request.getTeamId(), request.getRequestUserNo());
				
		if(isTeamMember != null) {
			responseWork.setType(isTeamMember);
			return responseWork;
		}
		
		if(isNullOrEmpty(request.getTitle()) ||
		   request.getTitle() == null ||	
		   isNullOrEmpty(request.getAssigneeNo().toString()) ||
		   isNullOrEmpty(request.getEndDate()) 
		   ) {
			responseWork.setType("빈 값이 있어 수정에 실패했습니다.");
			return responseWork;
		}
		
		Work work = Work.builder()
				.workId(request.getWorkId())
				.title(request.getTitle())
				.content(request.getContent())
				.assigneeNo(request.getAssigneeNo())
				.endDate(request.getEndDate())
				.build();
		
		int updateWorkDetail = workMapper.updateWorkDetail(work);
		
		if(updateWorkDetail == 0) {
			responseWork.setType("업무 정보 수정에 실패했습니다.");
			return responseWork;
		}
		
		responseWork = workMapper.findWorkByWorkId(request.getWorkId());
		responseWork.setType("update");
				
		return responseWork;
		
	}

	@Override
	public WorkDTO deleteWorkByWorkNo(WorkDTO request) {
		
		WorkDTO responseWork = new WorkDTO();
		responseWork.setRequestUserNo(request.getRequestUserNo());
		
		String isTeamMember = teamValidator.isTeamMember(request.getTeamId(), request.getRequestUserNo());
				
		if(isTeamMember != null) {
			responseWork.setType(isTeamMember);
			return responseWork;
		}
		
		int deleteWorkByWorkNo = workMapper.deleteWorkByWorkNo(request.getWorkId());
		
		if(deleteWorkByWorkNo == 0) {
			responseWork.setType("업무 삭제에 실패했습니다.");
			return responseWork;
		}
		
		return request;
	}
	
	private boolean isNullOrEmpty(String value) {
		return Optional.ofNullable(value)		// null값이면 orElse에 걸려 true 반환
					   .map(String::isBlank)	// null이 아닐 경우 isBlank 메서드를 이용해 빈 값인지 체크
					   .orElse(true);			
	}
	

}
