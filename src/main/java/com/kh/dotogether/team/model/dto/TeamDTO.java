package com.kh.dotogether.team.model.dto;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Validated
public class TeamDTO {
	
	private String teamId;

	private Long teamLeader;
	
	private String leaderName;
	
	private String leaderProfile;

	@Size(max=100, message="제목은 100자 이상 입력 불가능")
	@NotBlank(message="빈 값 입력은 불가능") 
	private String title;

	@Size(max=1000, message="내용은 1000자 이상 입력 불가능")
	@NotBlank(message="빈 값 입력은 불가능") 
	private String content;

	private String category;
	
	private String currentDate;

	private List<TeamMemberDTO> teamMemberList;

	private boolean canApplyToTeam ;
}
