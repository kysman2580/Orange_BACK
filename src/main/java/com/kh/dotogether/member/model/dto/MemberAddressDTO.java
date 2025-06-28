package com.kh.dotogether.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddressDTO {
	@NotBlank(message = "기본 주소는 필수 입력입니다.")
	private String userAddress1;
	
	@NotBlank(message = "상세 주소는 필수 입력입니다.")
	private String userAddress2;
}
