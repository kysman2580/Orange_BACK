package com.kh.dotogether.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.chat.model.dto.MessageDTO;
import com.kh.dotogether.chat.model.service.ChatService;
import com.kh.dotogether.util.ResponseData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
	
	private final ChatService chatService;
	
	
	@GetMapping
	public ResponseEntity<ResponseData> findMessagesByRoomId(@RequestParam(name="teamId") String teamId,
															 @RequestParam(name="lastTimeStamp") String lastTimeStamp){
		
		
		List<MessageDTO> messages = chatService.findMessagesByRoomId(teamId, lastTimeStamp);
		
		ResponseData responseData = ResponseData.builder()
												.code("S312")
												.message("메시지가 조회 되었습니다.")
												.items(messages)
												.build();	
		
		return ResponseEntity.ok(responseData);
	}
	
}
