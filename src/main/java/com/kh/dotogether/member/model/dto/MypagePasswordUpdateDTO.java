package com.kh.dotogether.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MypagePasswordUpdateDTO {
	@NotBlank(message = "새 비밀번호는 필수 입력입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@._^])[A-Za-z\\d!@._^]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자여야 합니다."
    )
	private String newPassword;
}
