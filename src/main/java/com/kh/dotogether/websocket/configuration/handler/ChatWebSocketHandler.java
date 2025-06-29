package com.kh.dotogether.websocket.configuration.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kh.dotogether.chat.model.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler{
	
	private final Map<String,Set<WebSocketSession>> rooms = new HashMap();
	private final ChatService chatService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

//		String roomId = getRoomId(session);
		
		log.info(">>>>>>>>>>>>>>>>>>> roomId : {}", "오니??????");
	}
	
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		super.handleTextMessage(session, message);
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
	}
	
	private String getRoomId(WebSocketSession session) {
		
		String path = session.getUri().getPath();
		
		return path.split("/")[path.length() - 1];
	}
}
