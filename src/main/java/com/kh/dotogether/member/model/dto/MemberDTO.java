package com.kh.dotogether.member.model.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
	private Long userNo;
	
	@NotBlank(message = "아이디는 필수 입력입니다.")
	@Pattern(
		    regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{6,15}$",
		    message = "아이디는 6~15자의 영문/숫자 조합만 가능합니다.")
	private String userId;
	
	@NotBlank(message = "비밀번호는 필수 입력입니다.")
	@Pattern(
	    regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@._^])[a-zA-Z\\d!@._^]{8,20}$",
	    message = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자여야 합니다."
	)
	private String userPw;
	
	@NotBlank(message = "이름은 필수 입력입니다.")
	@Pattern(
	    regexp = "^[a-zA-Z가-힣]{2,10}$",
	    message = "이름은 한글 또는 영문으로 2~10자여야 합니다."
	)
	private String userName;
	
	@NotBlank(message = "이메일은 필수 입력입니다.")
	@Pattern(
	    regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
	    message = "이메일 형식이 올바르지 않습니다."
	)
	private String userEmail;
	
	@NotBlank(message = "연락처는 필수 입력입니다.")
	@Pattern(
	    regexp = "^\\d{11}$",
	    message = "연락처는 11자리 숫자여야 합니다."
	)
	private String userPhone;
	
	@NotBlank(message = "주소는 필수 입력입니다.")
	private String userAddress1;
	
	@NotBlank(message = "상세 주소는 필수 입력입니다.")
	private String userAddress2;
	
	private String userRole;
	private String userStatus;
	private Date joinDate;
	private Date updateDate;
}
