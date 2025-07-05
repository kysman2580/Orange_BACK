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
    
    /**
     * 요청한 사용자의 팀이 5팀 이상인지 조회 (이상 : true | 미만 : false)
     */
    boolean countUserTeams(long userNo);
    
    /**
     * 팀 생성
     */
    int setTeam(Team team);
    
    /**
     * 팀 멤버로 추가
     */
    int setTeamMember(Team team);
    
    /**
     * 팀 목록 조회 (팀 정보 + 팀원 정보 포함)
     */
    List<TeamDTO> findTeamList(Team team);
    
    /**
     * 현재 로그인한 사용자가 생성한 팀 목록 조회
     */
    List<TeamDTO> findCreatedTeamSpaceByUserNo(Long userNo);
    
    /**
     * 현재 로그인한 사용자가 속한 팀 목록 조회 (팀장 제외)
     */
    List<TeamDTO> findTeamSpaceByUserNo(Long userNo);
    
    /**
     * 현재 로그인한 사용자가 속한 모든 팀 조회 (팀장 포함)
     */
    List<TeamDTO> findMyAllTeams(Long userNo);
    
    /**
     * 팀 존재 여부 검증 (존재하지 않으면 true, 존재하면 false)
     */
    boolean checkTeam(String teamId);
    
    /**
     * 팀 정원 초과 여부 검증 (초과하면 true, 미만이면 false)
     */
    boolean checkFullMember(String teamId);
    
    /**
     * 이미 팀원인지 검증 (팀원일 경우 true, 아닐 경우 false)
     */
    boolean checkAlreadyTeamMember(Team team);
    
    /**
     * 이미 팀 참가 신청한 기록이 있는지 검증 (있으면 true, 없으면 false)
     */
    boolean checkAlreadyApplied(Team team);
    
    /**
     * 팀 참가 신청 등록
     */
    int requestTeamJoin(Team team);
    
    /**
     * 현재 로그인한 팀장의 팀에 대한 참가 요청 목록 조회
     */
    List<ApplicantDTO> findTeamJoinRequests(Long userNo);
    
    /**
     * 팀 ID로 팀장 번호 조회
     */
    Long findTeamLeaderNo(String teamId);
    
    /**
     * 팀 참가 신청 삭제
     */
    int deleteTeamApplication(ApplicantDTO applicantInfo);
    
    /**
     * 팀 삭제
     */
    int deleteTeam(String teamId);
    
    /**
     * 특정 사용자(teamId 내) 팀원 여부 확인 (팀원일 경우 false, 아닐 경우 true)
     */
    boolean findMemberByUserNo(Team team);
    
    /**
     * 팀에서 특정 회원 탈퇴 처리
     */
    int deleteTeamMember(Team team);
    
    /**
     * 팀 ID로 팀 정보 조회 (팀 정보 + 팀원 정보 포함)
     */
    List<TeamDTO> findTeamInfoByTeamId(String teamId);
}
