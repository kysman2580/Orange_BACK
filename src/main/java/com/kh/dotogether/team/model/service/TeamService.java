package com.kh.dotogether.team.model.service;

import java.util.List;

import com.kh.dotogether.team.model.dto.ApplicantDTO;
import com.kh.dotogether.team.model.dto.TeamDTO;
import com.kh.dotogether.team.model.dto.TeamMemberDTO;


public interface TeamService {

	void setTeam(TeamDTO team);
	
	List<TeamDTO> findTeamList (String category, Long userNo, String lastTimeStamp);
	
	List<TeamDTO> findCreatedTeamSpaceByUserNo();
	
	List<TeamDTO> findTeamSpaceByUserNo();
	
	void requestTeamJoin(TeamDTO team);
	
	List<ApplicantDTO> findTeamJoinRequests();
	
	void acceptTeamJoin(ApplicantDTO applicantInfo);
	
	void cancleTeamJoin(ApplicantDTO applicantInfo);
	
	void deleteTeam(String teamId);
	
	void leaveTeam(String teamId);
	
	List<TeamDTO> findMyAllTeams();
	
	List<TeamDTO> findTeamInfoByTeamId(String teamId);
	
	void kickOutTeamMember(TeamMemberDTO temaMember);
}
