package com.kh.dotogether.team.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.dto.ApplicantDTO;
import com.kh.dotogether.team.model.dto.TeamDTO;
import com.kh.dotogether.team.model.dto.TeamMemberDTO;
import com.kh.dotogether.team.model.vo.Team;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeamServiceImpl implements TeamService {
	
	private final AuthService authService;
	private final TeamMapper teamMapper;
	
	@Override
	public void setTeam(TeamDTO team) {
		Long userNo = authService.getUserDetails().getUserNo();
		
		if(teamMapper.countUserTeams(userNo)) {
			throw new CustomException(ErrorCode.MAX_USER_TEAMS_EXCEEDED);
		}
		
		String teamId = UUID.randomUUID().toString(); 
		
		Team teamVO = Team.builder().teamId(teamId)
									.userNo(userNo)
									.title(team.getTitle())
									.content(team.getContent())
									.category(team.getCategory())
									.build();
		
		int setTeam = teamMapper.setTeam(teamVO);
		
		if(setTeam != 1) {
			throw new CustomException(ErrorCode.INSERT_FAILED);
		}
		
		int setTeamMember = teamMapper.setTeamMember(teamVO);
		
		if(setTeamMember != 1) {
			throw new CustomException(ErrorCode.INSERT_FAILED);
		}
		
		return;
	}
	
	
	public List<TeamDTO> findTeamList (String category, Long userNo, String lastTimeStamp){
		
		
		Team team = Team.builder().userNo(userNo)
								  .category(category)
								  .lastTimeStamp(lastTimeStamp)
								  .build();
		
		List<TeamDTO> teamList = teamMapper.findTeamList(team);
		
		return teamList;
	}


	@Override
	public List<TeamDTO> findCreatedTeamSpaceByUserNo() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findCreatedTeamSpaceByUserNo(userNo);
		
		return teamList;
	}
	
	@Override
	public List<TeamDTO> findTeamSpaceByUserNo() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findTeamSpaceByUserNo(userNo);
		
		return teamList;
	}


	@Override
	public void requestTeamJoin(TeamDTO team) {
		
		if(teamMapper.checkTeam(team.getTeamId())) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		if(teamMapper.checkFullMember(null)) {
			throw new CustomException(ErrorCode.TEAM_FULL);
		}
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Team teamVO = Team.builder().teamId(team.getTeamId())
									.userNo(userNo)
									.build();

		if(teamMapper.checkAlreadyTeamMember(teamVO)) {
			throw new CustomException(ErrorCode.ALREADY_TEAM_MEMBER);
		}
		
		if(teamMapper.checkAlreadyApplied(teamVO)) {
			throw new CustomException(ErrorCode.ALREADY_APPLIED);
		}
		
		int check = teamMapper.requestTeamJoin(teamVO);
		
		if(check < 1) {
			throw new CustomException(ErrorCode.INSERT_FAILED);
		}
		
		return;
	}


	@Override
	public List<ApplicantDTO> findTeamJoinRequests() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<ApplicantDTO> applicantList = teamMapper.findTeamJoinRequests(userNo);
		
		return applicantList;
	}


	@Override
	public void acceptTeamJoin(ApplicantDTO applicantInfo) {
		
		String teamId = applicantInfo.getTeamId();
		Long userNo = authService.getUserDetails().getUserNo();
		
		isTeamLeader(teamId, userNo);
		
		if(teamMapper.checkFullMember(teamId)) {
			throw new CustomException(ErrorCode.TEAM_FULL);
		}
		
		if(teamMapper.countUserTeams(userNo)) {
			throw new CustomException(ErrorCode.MAX_USER_TEAMS_EXCEEDED);
		}
		
		Team teamVO = Team.builder().teamId(teamId)
									.userNo(applicantInfo.getApplicantNo())
									.build();
		
		int setTeamMember = teamMapper.setTeamMember(teamVO);
		
		if(setTeamMember != 1) {
			throw new CustomException(ErrorCode.INSERT_FAILED);
		}
		
		int deleteApplicantion = teamMapper.deleteTeamApplication(applicantInfo.getRequestNo());
		
		if(deleteApplicantion != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}
	
	
	
	@Override
	public void cancleTeamJoin(ApplicantDTO applicantInfo) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Long teamLeader = teamMapper.findTeamLeaderNo(applicantInfo.getTeamId());
		
		if(!userNo.equals(teamLeader)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
		
		int deleteApplicantion = teamMapper.deleteTeamApplication(applicantInfo.getRequestNo());
		
		if(deleteApplicantion != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}
	

	@Override
	public void deleteTeamInfo(String teamId) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		isTeamLeader(teamId, userNo);
		
		int deleteTeam = teamMapper.deleteTeamInfo(teamId);
		
		if(deleteTeam != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}


	@Override
	public void leaveTeam(String teamId) {

		Long userNo = authService.getUserDetails().getUserNo();
		
		Team teamVO = Team.builder()
						  .teamId(teamId)
						  .userNo(userNo)
						  .build();
		
		isTeamMember(teamVO);
		
		Long teamLeader = teamMapper.findTeamLeaderNo(teamId);
		
		if(teamLeader.equals(userNo)) {
			throw new CustomException("E302", "팀장은 팀 탈퇴가 불가능합니다.");
		}
		
		int deleteTeamMember = teamMapper.deleteTeamMember(teamVO);
		
		if(deleteTeamMember != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
	}
	
	@Override
	public List<TeamDTO> findMyAllTeams() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findMyAllTeams(userNo);
		
		return teamList;
	}
	
	
	@Override
	public List<TeamDTO> findTeamInfoByTeamId(String teamId) {
		
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Team teamVO = Team.builder()
						  .userNo(userNo)
						  .teamId(teamId)
						  .build();
		
		isTeamMember(teamVO);

		List<TeamDTO> TeamInfo = teamMapper.findTeamInfoByTeamId(teamId);
		
		return TeamInfo;
	}
	
	@Override
	public void kickOutTeamMember(TeamMemberDTO temaMember) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		isTeamLeader(temaMember.getTeamId(), userNo);
		
		
		if(userNo.equals(temaMember.getMemberNo())) {
			throw new CustomException(ErrorCode.TEAM_LEADER_CANNOT_LEAVE);
		}
		
		Team teamVO = Team.builder()
				  		  .teamId(temaMember.getTeamId())
				  		  .userNo(temaMember.getMemberNo())
				  		  .build();

		int deleteTeamMember = teamMapper.deleteTeamMember(teamVO);
		
		if(deleteTeamMember != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
	}
	
	
	private void isTeamLeader(String teamId, Long userNo) {
		
		if(teamMapper.checkTeam(teamId)) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		Long teamLeader = teamMapper.findTeamLeaderNo(teamId);
		
		if(!userNo.equals(teamLeader)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
	}
	
	private void isTeamMember(Team teamVO) {
		
		if(teamMapper.checkTeam(teamVO.getTeamId())) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		if(teamMapper.findMemberByUserNo(teamVO)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
	}


}
