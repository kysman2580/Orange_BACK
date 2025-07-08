package com.kh.dotogether.challenge.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Challenge {
	
	private Long challengeNo;
    private String challengeTitle;
    private String challengeContent;
    private String challengeAuthor;
    private String challengeComment;
    private String challengeFileUrl;
    private String status;
    private Date challengeDate;
    
}