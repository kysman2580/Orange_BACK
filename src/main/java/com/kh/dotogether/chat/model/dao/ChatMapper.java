package com.kh.dotogether.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.dotogether.chat.model.vo.GetMessagesRequest;
import com.kh.dotogether.chat.model.vo.Message;
import com.kh.dotogether.chat.model.dto.MessageDTO;

@Mapper
public interface ChatMapper {
	
	List<MessageDTO> findMessagesByRoomId(GetMessagesRequest request);
	
	int sendChatMessage(Message message);
	
	MessageDTO findMessageByMessageId(String messageId);
	
}
