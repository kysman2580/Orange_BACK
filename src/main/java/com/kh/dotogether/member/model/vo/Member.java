package com.kh.dotogether.member.model.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Getter
@Setter
public class Member {
	
	private Long userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String userEmail;
	private String userPhone;
	private String userAddress1;
	private String userAddress2;
	private String userRole;
	private String userStatus;
	private Date joinDate;
	private Date updateDate;
}
