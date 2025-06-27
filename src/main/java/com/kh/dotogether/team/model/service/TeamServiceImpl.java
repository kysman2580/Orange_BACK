package com.kh.dotogether.team.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.dto.TeamDTO;
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

		boolean canCreateTeam = teamMapper.countUserTeams(userNo);
		
		if(canCreateTeam) {
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
	
	
	
}
