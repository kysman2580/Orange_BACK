package com.kh.dotogether.team.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.dotogether.team.model.dto.ApplicantDTO;
import com.kh.dotogether.team.model.dto.TeamDTO;
import com.kh.dotogether.team.model.dto.TeamMemberDTO;
import com.kh.dotogether.team.model.vo.Team;

@Mapper
public interface TeamMapper {
	
	boolean countUserTeams(long userNo);
	
	int setTeam(Team team);
	
	int setTeamMember(Team team);
	
	List<TeamDTO> findTeamList(Team team);
	
	List<TeamDTO> findCreatedTeamSpaceByUserNo(Long userNo);
	
	List<TeamDTO> findTeamSpaceByUserNo(Long userNo);
	
	List<TeamDTO> findMyAllTeams(Long userNo);
	
	boolean checkTeam(String teamId);
	
	boolean checkFullMember(String teamId);
	
	boolean checkAlreadyTeamMember(Team team);
	
	boolean checkAlreadyApplied(Team team);
	
	int requestTeamJoin(Team team);
	
	List<ApplicantDTO> findTeamJoinRequests(Long userNo);
	
	Long findTeamLeaderNo(String teamId);
	
	int deleteTeamApplication(Long requestNo);
	
	int deleteTeamInfo(String teamId);
	
	boolean findMemberByUserNo(Team team);
	
	int deleteTeamMember(Team team);
	
	List<TeamDTO> findTeamInfoByTeamId(String teamId);
}
