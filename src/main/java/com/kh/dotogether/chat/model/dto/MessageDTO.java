package com.kh.dotogether.chat.model.dto;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class MessageDTO {

	private String teamId;

	private String messageId;

	private Long senderNo;

	private String senderName;

	private String content;

	private String sentDate;

	private String type;
	
}
