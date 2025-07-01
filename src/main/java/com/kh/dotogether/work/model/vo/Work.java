package com.kh.dotogether.work.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Work {

	private String teamId;

	private String workId;

	private String title;

	private String content;

	private Long assigneeNo;

	private String endDate;
	
	private String status;
}
