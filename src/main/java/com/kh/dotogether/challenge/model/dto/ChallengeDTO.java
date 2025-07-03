package com.kh.dotogether.challenge.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDTO {
	
	private Long challengeNo;
    private String challengeTitle;
    private String challengeContent;
    private String challengeAuthor;
    private String challengeComment;
    private String challengeFileUrl;
    private String status;
    private Date challengeDate;
    
}