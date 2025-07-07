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
import com.kh.dotogether.team.util.DeleteTeamApplication;
import com.kh.dotogether.team.util.TeamValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeamServiceImpl implements TeamService {
	
	private final AuthService authService;
	private final TeamMapper teamMapper;
	private final TeamValidator teamValidator;
	private final DeleteTeamApplication deleteApplication;
	
	
	/**
	 * 새로운 팀을 생성하는 메소드
	 * <p>
	 * 한 사용자는 최대 5개의 팀만 생성할 수 있으며, 그 이상은 생성 불가능
	 * </p>
	 * 
	 * @param team 생성할 팀의 이름, 설명, 리더정보, 카테고리 등을 담고 있는 DTO 객체
	 * @throws MAX_USER_TEAMS_EXCEEDED 사용자가 생성 가능한 팀 수(5개)를 초과한 경우
	 * @throws INSERT_FAILED 팀 생성 또는 팀원 등록에 실패한 경우
	 */
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
	
	
	/**
	 * 카테고리에 맞는 팀 목록 조회
	 * 
	 * <p>
	 * 특정 카테고리 팀 목록을 10개씩 페이징하여 조회(cursor방식) 
	 * 각 팀의 팀장 정보와 팀원 목록, 그리고 현재 사용자가 해당 팀에 지원 가능한지 여부도 포함.
	 * </p>
	 *
	 * @param category 조회할 팀의 카테고리 (예: "all"은 전체 카테고리)
	 * @param userNo 조회를 요청한 사용자의 고유 번호 (팀 지원 가능 여부 판단에 사용)
	 * @param lastTimeStamp cursor방식을 위한 값 (null이면 최신부터 조회)
	 * @return List<TeamDTO> 팀 정보와 팀원 정보를 포함한 팀 목록
	 */
	public List<TeamDTO> findTeamList (String category, Long userNo, String lastTimeStamp){
		
		
		Team team = Team.builder().userNo(userNo)
								  .category(category)
								  .lastTimeStamp(lastTimeStamp)
								  .build();
		
		List<TeamDTO> teamList = teamMapper.findTeamList(team);
		
		return teamList;
	}


	/**
	 * 현재 로그인한 사용자가 생성한 팀 목록 조회
	 *
	 * <p>
	 * 해당 사용자가 팀장으로 있는 팀 목록을 반환
	 * </p>
	 *
	 * @return 사용자가 생성한 팀 정보 목록
	 */
	@Override
	public List<TeamDTO> findCreatedTeamSpaceByUserNo() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findCreatedTeamSpaceByUserNo(userNo);
		
		return teamList;
	}
	
	
	/**
	 * 현재 로그인한 사용자가 소속된 팀 목록 조회
	 *
	 * <p>
	 * 팀장 또는 팀원으로 참여 중인 팀 목록을 반환
	 * </p>
	 *
	 * @return 사용자가 참여 중인 팀 정보 목록
	 */
	@Override
	public List<TeamDTO> findTeamSpaceByUserNo() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findTeamSpaceByUserNo(userNo);
		
		return teamList;
	}


	/**
	 * 팀 참가 요청 처리
	 *
	 * <p>
	 * 팀 존재 여부, 인원 초과 여부, 중복 참가 여부, 중복 신청 여부 확인 후 참가 신청 등록  
	 * </p>
	 *
	 * @param team 참가 요청할 팀의 ID를 담고 있는 DTO 객체
	 * @throws TEAM_NOT_FOUND 팀이 존재하지 않는 경우
	 * @throws TEAM_FULL 팀 인원이 가득 찬 경우
	 * @throws ALREADY_TEAM_MEMBER 이미 팀에 소속된 경우
	 * @throws ALREADY_APPLIED 이미 참가 신청한 경우
	 * @throws INSERT_FAILED 참가 요청 등록에 실패한 경우
	 */
	@Override
	public void requestTeamJoin(TeamDTO team) {
		
		if(teamMapper.checkTeam(team.getTeamId())) {
			throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
		}
		
		if(teamMapper.checkFullMember(team.getTeamId())) {
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


	/**
	 * 현재 로그인한 사용자의 팀 참가 요청 목록 조회
	 *
	 * <p>
	 * 사용자가 팀장으로 있는 팀들에 들어온 참가 요청 목록 반환
	 * </p>
	 *
	 * @return 팀 참가 요청자 정보 목록
	 */
	@Override
	public List<ApplicantDTO> findTeamJoinRequests() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<ApplicantDTO> applicantList = teamMapper.findTeamJoinRequests(userNo);
		
		return applicantList;
	}


	/**
	 * 팀 참가 요청 수락 처리
	 *
	 * <p>
	 * 팀장 권한 확인 후, 인원 제한 및 참가자 조건 확인  
	 * 조건 충족 시 팀원으로 등록하고 참가 신청 삭제  
	 * 조건 불충족 시 신청 삭제 후 예외 발생
	 * </p>
	 *
	 * @param applicantInfo 참가 요청자의 팀 ID 및 사용자 정보를 담은 DTO 객체
	 * @throws TEAM_FULL 팀 인원이 가득 찬 경우
	 * @throws MAX_USER_TEAMS_EXCEEDED 참가자가 최대 팀 수를 초과한 경우
	 * @throws INSERT_FAILED 팀원 등록에 실패한 경우
	 * @throws DELETE_FAILED 참가 신청 삭제에 실패한 경우
	 */
	@Override
	public void acceptTeamJoin(ApplicantDTO applicantInfo) {
		
		String teamId = applicantInfo.getTeamId();
		Long userNo = authService.getUserDetails().getUserNo();
		
		teamValidator.isTeamLeader(teamId, userNo);
		
		if(teamMapper.checkFullMember(teamId)) {
			deleteApplication.deleteTeamApplication(applicantInfo);
			throw new CustomException(ErrorCode.TEAM_FULL);
		}
		
		if(teamMapper.countUserTeams(applicantInfo.getApplicantNo())) {
			deleteApplication.deleteTeamApplication(applicantInfo);
			throw new CustomException(ErrorCode.MAX_USER_TEAMS_EXCEEDED);
		}
		
		Team teamVO = Team.builder().teamId(teamId)
									.userNo(applicantInfo.getApplicantNo())
									.build();
		
		int setTeamMember = teamMapper.setTeamMember(teamVO);
		
		if(setTeamMember != 1) {
			throw new CustomException(ErrorCode.INSERT_FAILED);
		}
		
		int deleteApplicantion = teamMapper.deleteTeamApplication(applicantInfo);
		
		if(deleteApplicantion != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}
	
	
	/**
	 * 팀 참가 요청 취소 처리
	 *
	 * <p>
	 * 팀장 권한 확인 후, 참가 신청을 삭제
	 * </p>
	 *
	 * @param applicantInfo 참가 요청 정보를 담은 DTO 객체
	 * @throws TEAM_UNAUTHORIZED_USER 팀장이 아닌 사용자가 요청한 경우
	 * @throws DELETE_FAILED 참가 신청 삭제 실패 시 발생
	 */
	@Override
	public void cancleTeamJoin(ApplicantDTO applicantInfo) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Long teamLeader = teamMapper.findTeamLeaderNo(applicantInfo.getTeamId());
		
		if(!userNo.equals(teamLeader)) {
			throw new CustomException(ErrorCode.TEAM_UNAUTHORIZED_USER);
		}
		
		int deleteApplicantion = teamMapper.deleteTeamApplication(applicantInfo);
		
		if(deleteApplicantion != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}
	

	/**
	 * 팀 삭제 처리
	 *
	 * <p>
	 * 팀장 권한 확인 후, 해당 팀 삭제
	 * </p>
	 *
	 * @param teamId 삭제할 팀의 ID
	 * @throws DELETE_FAILED 팀 삭제에 실패한 경우
	 */
	@Override
	public void deleteTeam(String teamId) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		teamValidator.isTeamLeader(teamId, userNo);
		
		int deleteTeam = teamMapper.deleteTeam(teamId);
		
		if(deleteTeam != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
		
		return;
	}


	/**
	 * 팀 탈퇴 처리
	 *
	 * <p>
	 * 팀원 권한 확인 후, 팀장이면 탈퇴 불가  
	 * 팀원이면 팀에서 탈퇴 처리
	 * </p>
	 *
	 * @param teamId 탈퇴할 팀의 ID
	 * @throws "E302" 팀장이 탈퇴를 시도할 경우
	 * @throws DELETE_FAILED 팀 탈퇴 처리 실패 시 발생
	 */
	@Override
	public void leaveTeam(String teamId) {

		Long userNo = authService.getUserDetails().getUserNo();
		
		Team teamVO = Team.builder()
						  .teamId(teamId)
						  .userNo(userNo)
						  .build();
		
		teamValidator.isTeamMember(teamVO);
		
		Long teamLeader = teamMapper.findTeamLeaderNo(teamId);
		
		if(teamLeader.equals(userNo)) {
			throw new CustomException("E302", "팀장은 팀 탈퇴가 불가능합니다.");
		}
		
		int deleteTeamMember = teamMapper.deleteTeamMember(teamVO);
		
		if(deleteTeamMember != 1) {
			throw new CustomException(ErrorCode.DELETE_FAILED);
		}
	}
	
	/**
	 * 현재 로그인한 사용자가 속한 모든 팀 목록 조회
	 *
	 * <p>
	 * 팀장 또는 팀원으로 참여 중인 모든 팀 정보를 반환
	 * </p>
	 *
	 * @return 사용자가 속한 팀 정보 목록
	 */
	@Override
	public List<TeamDTO> findMyAllTeams() {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		List<TeamDTO> teamList = teamMapper.findMyAllTeams(userNo);
		
		return teamList;
	}
	
	
	/**
	 * 팀 ID로 팀 정보 조회
	 *
	 * <p>
	 * 로그인한 사용자가 팀원인지 확인 후, 해당 팀 정보를 반환
	 * </p>
	 *
	 * @param teamId 조회할 팀의 ID
	 * @return 팀 정보 목록
	 */
	@Override
	public List<TeamDTO> findTeamInfoByTeamId(String teamId) {
		
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Team teamVO = Team.builder()
						  .userNo(userNo)
						  .teamId(teamId)
						  .build();
		
		teamValidator.isTeamMember(teamVO);

		List<TeamDTO> TeamInfo = teamMapper.findTeamInfoByTeamId(teamId);
		
		return TeamInfo;
	}
	
	
	/**
	 * 팀원 추방
	 *
	 * <p>
	 * 팀장 권한 확인 후, 본인(팀장)은 추방 불가 처리  
	 * 대상 팀원을 팀에서 추방
	 * </p>
	 *
	 * @param temaMember 추방 대상 팀원 정보 DTO
	 * @throws TEAM_LEADER_CANNOT_LEAVE 팀장이 본인을 추방시키려 할 경우
	 * @throws DELETE_FAILED 팀원 추방 처리 실패 시 발생
	 */
	@Override
	public void kickOutTeamMember(TeamMemberDTO temaMember) {
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		teamValidator.isTeamLeader(temaMember.getTeamId(), userNo);
		
		
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

}
