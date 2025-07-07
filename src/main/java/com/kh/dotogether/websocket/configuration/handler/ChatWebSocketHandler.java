package com.kh.dotogether.websocket.configuration.handler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.chat.model.dto.MessageDTO;
import com.kh.dotogether.chat.model.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler{
	
	private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
	// HashMap 대신 ConcurrentHashMap 사용 이유 => 멀티스레드 환경에서의 안전한 동시 접근
	// ConcurrentModificationException 발생 방지
	private final ChatService chatService;
	private final AuthService authService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		String roomId = getRoomId(session);
		
		if(roomId == null || "".equals(roomId)) {
			session.close(CloseStatus.BAD_DATA);
			return;
		}
		
		rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);			
		
	}
	
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		MessageDTO chatMessage = objectMapper.readValue(message.getPayload(), MessageDTO.class);
		
		String roomId = getRoomId(session);
		
		switch(chatMessage.getType()) {
		case "send": 
			chatMessage = chatService.sendChatMessage(chatMessage);
			break;
		case "update":
			chatMessage = chatService.updateChatMessage(chatMessage);
			break;
		case "delete":
			chatMessage = chatService.deleteChatMessage(chatMessage);
			break;
		default : break;
		}
		
		TextMessage textMessage = 
				new TextMessage(objectMapper.writeValueAsString(chatMessage));
				
		for(WebSocketSession user: rooms.getOrDefault(roomId, Collections.emptySet())) {
			if(user.isOpen()) {
				user.sendMessage(textMessage);
			}
		}
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

	}
	
	private String getRoomId(WebSocketSession session) {
		
		String path = session.getUri().getPath();
		
		String[] endPiont = path.split("/");
		
		return endPiont[endPiont.length - 1];
	}
}
