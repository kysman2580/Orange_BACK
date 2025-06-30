package com.kh.dotogether.chat.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kh.dotogether.auth.service.AuthService;
import com.kh.dotogether.chat.model.dao.ChatMapper;
import com.kh.dotogether.chat.model.dto.MessageDTO;
import com.kh.dotogether.chat.model.vo.GetMessagesRequest;
import com.kh.dotogether.chat.model.vo.Message;
import com.kh.dotogether.team.model.dao.TeamMapper;
import com.kh.dotogether.team.model.vo.Team;
import com.kh.dotogether.team.util.TeamValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	
	private final TeamValidator teamValidator;
	private final AuthService authService;
	private final ChatMapper chatMapper;
	private final TeamMapper teamMapper;
	
	
	public List<MessageDTO> findMessagesByRoomId(String teamId, String lastTimeStamp){
		
		GetMessagesRequest request = GetMessagesRequest.builder()
															 .teamId(teamId)
															 .lastTimeStamp(lastTimeStamp)
															 .build();
		
		Long userNo = authService.getUserDetails().getUserNo();
		
		Team team = Team.builder()
						.userNo(userNo)
						.teamId(request.getTeamId())
						.build();
		
		
		teamValidator.isTeamMember(team);
		
		List<MessageDTO> messages = chatMapper.findMessagesByRoomId(request);
		
		return messages;
	}


	@Override
	public MessageDTO sendChatMessage(MessageDTO message) {

		String messageId = UUID.randomUUID().toString(); 
		String content = message.getContent();
		MessageDTO responseMessage = new MessageDTO();
		
		if(content == null || "".equals(content)){
			responseMessage.setType("빈 문자 메시지는 전송이 불가능합니다.");
			return responseMessage;
		}
		
		Team team = Team.builder()
						.userNo(message.getSenderNo())
						.teamId(message.getTeamId())
						.build();
		
		
		if(teamMapper.checkTeam(team.getTeamId())) {
			responseMessage.setType("요청 보낸 팀이 존재하지 않습니다.");
			return responseMessage;
		}
		
		if(teamMapper.findMemberByUserNo(team)) {
			responseMessage.setType("요청 보낸 사용자는 팀원이 아닙니다.");
			return responseMessage;
		}
		
		Message messageVO = Message.builder()
								   .messageId(messageId)
								   .teamId(message.getTeamId())
								   .senderNo(message.getSenderNo())
								   .content(message.getContent())
								   .build();
		
		int sendChatMessage = chatMapper.sendChatMessage(messageVO);
		
		if(sendChatMessage == 0) {
			responseMessage.setType("메시지 전송에 실패했습니다.");
			return responseMessage;
		}
		
		responseMessage = chatMapper.findMessageByMessageId(messageId);
		
		responseMessage.setType("send");
		
		return responseMessage;
	}


	@Override
	public MessageDTO updateChatMessage(MessageDTO message) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MessageDTO deleteChatMessage(MessageDTO message) {
		// TODO Auto-generated method stub
		return null;
	}

}
