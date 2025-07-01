package com.kh.dotogether.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoUpdateDTO {
	private String userPhone;
	private String userEmail;
	private String userAddress1;
	private String userAddress2;
}
