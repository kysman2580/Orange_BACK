package com.kh.dotogether.token.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshToken {
	private Long tokenNo; // 토큰 번호
	private Long userNo; // 유저 번호
	private String token; // 토큰
	private Date expireAt; // 유효 시간
}
