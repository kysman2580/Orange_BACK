package com.kh.dotogether.team.model.service;

import java.util.List;

import com.kh.dotogether.team.model.dto.TeamDTO;

public interface TeamService {

	void setTeam(TeamDTO team);
	
	List<TeamDTO> findTeamList (String category, Long userNo, String lastTimeStamp);
	
	List<TeamDTO> findCreatedTeamSpaceByUserNo();
	
	List<TeamDTO> findTeamSpaceByUserNo();
	
	void requestTeamJoin(TeamDTO team);
}
