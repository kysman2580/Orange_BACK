package com.kh.dotogether.log.model.dto;

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
public class LogDTO {
	private Long logNo;
	private Long logUserNo;
    private String logValue;
    private Date logDate;
}