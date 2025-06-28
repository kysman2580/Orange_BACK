package com.kh.dotogether.team.model.service;

import java.util.List;

import com.kh.dotogether.team.model.dto.ApplicantDTO;
import com.kh.dotogether.team.model.dto.TeamDTO;
<<<<<<< HEAD
import com.kh.dotogether.team.model.dto.TeamMemberDTO;
=======
>>>>>>> faaf4c8e3d5953cd1bc402e5e55a34f831d02436

public interface TeamService {

	void setTeam(TeamDTO team);
	
	List<TeamDTO> findTeamList (String category, Long userNo, String lastTimeStamp);
	
	List<TeamDTO> findCreatedTeamSpaceByUserNo();
	
	List<TeamDTO> findTeamSpaceByUserNo();
	
	void requestTeamJoin(TeamDTO team);
	
	List<ApplicantDTO> findTeamJoinRequests();
<<<<<<< HEAD
	
	void acceptTeamJoin(ApplicantDTO applicantInfo);
	
	void cancleTeamJoin(ApplicantDTO applicantInfo);
	
	void deleteTeamInfo(String teamId);
	
	void leaveTeam(String teamId);
	
	List<TeamDTO> findMyAllTeams();
	
	List<TeamDTO> findTeamInfoByTeamId(String teamId);
	
	void kickOutTeamMember(TeamMemberDTO temaMember);
	
=======
>>>>>>> faaf4c8e3d5953cd1bc402e5e55a34f831d02436
}
