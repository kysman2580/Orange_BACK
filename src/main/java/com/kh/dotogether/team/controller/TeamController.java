package com.kh.dotogether.team.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.team.model.dto.ApplicantDTO;
import com.kh.dotogether.team.model.dto.TeamDTO;
import com.kh.dotogether.team.model.dto.TeamMemberDTO;
import com.kh.dotogether.team.model.service.TeamService;
import com.kh.dotogether.util.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
	
	private final TeamService teamService;
	
	@PostMapping
	public ResponseEntity<ResponseData> setTeam(@RequestBody TeamDTO team){
		
		teamService.setTeam(team);
		
		ResponseData responseData = ResponseData.builder()
												.code("S300")
												.message("팀이 생성되었습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping
	public ResponseEntity<ResponseData> findTeamList(@RequestParam(name="category") String category,
													 @RequestParam(name="userNo") Long userNo,
													 @RequestParam(name="lastTimeStamp") String lastTimeStamp){
		
		List<TeamDTO> teamList = teamService.findTeamList(category, userNo, lastTimeStamp);
		
		ResponseData responseData = ResponseData.builder()
												.code("S310")
												.message("팀이 조회되었습니다.")
												.items(teamList)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping("/created-team")
	public ResponseEntity<ResponseData> findCreatedTeamSpaceByUserNo(){
		
		List<TeamDTO> teamList = teamService.findCreatedTeamSpaceByUserNo();
		
		ResponseData responseData = ResponseData.builder()
												.code("S310")
												.message("팀이 조회되었습니다.")
												.items(teamList)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping("/affiliated")
	public ResponseEntity<ResponseData> findTeamSpaceByUserNo(){
		
		List<TeamDTO> teamList = teamService.findTeamSpaceByUserNo();
		
		ResponseData responseData = ResponseData.builder()
												.code("S310")
												.message("팀이 조회되었습니다.")
												.items(teamList)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@PostMapping("/join")
	public ResponseEntity<ResponseData> requestTeamJoin(@RequestBody TeamDTO team){
		
		teamService.requestTeamJoin(team);
		
		ResponseData responseData = ResponseData.builder()
												.code("S301")
												.message("팀 신청에 성공하였습니다.")
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@GetMapping("/member")
	public ResponseEntity<ResponseData> findTeamJoinRequests(){
		
		List<ApplicantDTO> applicantList = teamService.findTeamJoinRequests();
		
		ResponseData responseData = ResponseData.builder()
												.code("S310")
												.message("신청 정보가 조회되었습니다.")
												.items(applicantList)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@PostMapping("/join-accept")
	public ResponseEntity<ResponseData> acceptTeamJoin(@RequestBody ApplicantDTO applicantInfo){
		
		teamService.acceptTeamJoin(applicantInfo);
		
		ResponseData responseData = ResponseData.builder()
												.code("S302")
												.message("요청을 수락하여 팀원이 추가되었습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	@DeleteMapping("/join-cancle")
	public ResponseEntity<ResponseData> cancleTeamJoin(@RequestBody ApplicantDTO applicantInfo){
		
		teamService.cancleTeamJoin(applicantInfo);
		
		ResponseData responseData = ResponseData.builder()
												.code("S330")
												.message("요청을 거절했습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	
	@DeleteMapping()
	public ResponseEntity<ResponseData> deleteTeamInfo(@RequestParam(name="teamId") String teamId){
		
		teamService.deleteTeamInfo(teamId);
		
		ResponseData responseData = ResponseData.builder()
												.code("S331")
												.message("팀이 정상적으로 삭제되었습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	@DeleteMapping("/my-team")
	public ResponseEntity<ResponseData> leaveTeam(@RequestParam(name="teamId") String teamId){
		
		teamService.leaveTeam(teamId);
		
		ResponseData responseData = ResponseData.builder()
												.code("S331")
												.message("팀이 정상적으로 삭제되었습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	
	@GetMapping("/my-team")
	public ResponseEntity<ResponseData> findMyAllTeams(){
		
		List<TeamDTO> teamList = teamService.findMyAllTeams();
		
		ResponseData responseData = ResponseData.builder()
												.code("S310")
												.message("팀이 조회되었습니다.")
												.items(teamList)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	
	@GetMapping("/{teamId}")
	public ResponseEntity<ResponseData> findTeamInfoByTeamId(@PathVariable(name="teamId") String teamId){
		
		List<TeamDTO> TeamInfo = teamService.findTeamInfoByTeamId(teamId);
		
		ResponseData responseData = ResponseData.builder()
												.code("S311")
												.message("팀 정보가 조회되었습니다.")
												.items(TeamInfo)
												.build();

		return ResponseEntity.ok(responseData);
	}
	
	@DeleteMapping("/member")
	public ResponseEntity<ResponseData> kickOutTeamMember(@RequestBody TeamMemberDTO teamMember){
		
		teamService.kickOutTeamMember(teamMember);
		
		ResponseData responseData = ResponseData.builder()
												.code("S331")
												.message(teamMember.getMemberName() + "님이 추방되었습니다.")
												.build();
		
		return ResponseEntity.ok(responseData);
	}
	
	
}
