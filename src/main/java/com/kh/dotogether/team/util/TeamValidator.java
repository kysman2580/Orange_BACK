package com.kh.dotogether.team.util;

import org.springframework.stereotype.Component;

import com.kh.dotogether.exception.exceptions.CustomException;
import com.kh.dotogether.global.enums.ErrorCode;
import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.vo.Team;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamValidator {
	
	private final TeamMapper teamMapper;

	public void isTeamLeader(String teamId, Long userNo) {
		
		if(teamMapper.checkTeam(teamId)) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		Long teamLeader = teamMapper.findTeamLeaderNo(teamId);
		
		if(!userNo.equals(teamLeader)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
	}
	
	public void isTeamMember(Team teamVO) {
		
		if(teamMapper.checkTeam(teamVO.getTeamId())) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		if(teamMapper.findMemberByUserNo(teamVO)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
	}
}
