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
public class MemberPhoneDTO {
	@NotBlank(message = "연락처는 필수 입력입니다.")
	private String userPhone;
}
