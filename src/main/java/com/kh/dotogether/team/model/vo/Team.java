package com.kh.dotogether.team.model.vo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Team {
	
	private Long userNo;
	
	private String teamId;

	private Long teamLeader;

	private String title;
	
	private String content;

	private String category;
	
	private String lastTimeStamp;
}
