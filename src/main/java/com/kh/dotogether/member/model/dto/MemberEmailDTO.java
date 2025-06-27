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
public class MemberEmailDTO {
	@NotBlank(message = "이메일은 필수 입력입니다.")
	private String userEmail;
}
