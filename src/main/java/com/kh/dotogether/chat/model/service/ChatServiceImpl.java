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
		responseMessage.setSenderNo(message.getSenderNo());
		
		String checkContent = message.getContent().replaceAll("\\n", "").trim();
		
		if(checkContent == null || "".equals(checkContent)){
			responseMessage.setType("빈 문자 메시지는 전송이 불가능합니다.");
			return responseMessage;
		}
		
		Team team = Team.builder()
						.userNo(message.getSenderNo())
						.teamId(message.getTeamId())
						.build();
		
		
		if(teamMapper.checkTeam(message.getTeamId())) {
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
	
		String content = message.getContent();
		MessageDTO responseMessage = new MessageDTO();
		responseMessage.setSenderNo(message.getSenderNo());
		
		String checkContent = message.getContent().replaceAll("\\n", "").trim();
		
		if(checkContent == null || "".equals(checkContent)){
			responseMessage.setType("빈 문자 메시지는 전송이 불가능합니다.");
			return responseMessage;
		}
		
		Team team = Team.builder()
						.userNo(message.getSenderNo())
						.teamId(message.getTeamId())
						.build();
		
		if(teamMapper.checkTeam(message.getTeamId())) {
			responseMessage.setType("요청 보낸 팀이 존재하지 않습니다.");
			return responseMessage;
		}
		
		if(teamMapper.findMemberByUserNo(team)) {
			responseMessage.setType("요청 보낸 사용자는 팀원이 아닙니다.");
			return responseMessage;
		}
		
		if(chatMapper.checkIsSender(message)) {
			responseMessage.setType("발신자가 아니면 수정이 불가 합니다.");
			return responseMessage;
		}
		
		if(chatMapper.checkMessage(message.getMessageId())) {
			responseMessage.setType("요청 보낸 메시지가 존재하지 않습니다.");
			return responseMessage;
		}
		
		int updateChatMessage = chatMapper.updateChatMessage(message);
		
		if(updateChatMessage == 0) {
			responseMessage.setType("메시지 수정에 실패했습니다.");
			return responseMessage;
		}
		
		responseMessage.setMessageId(message.getMessageId());
		responseMessage.setContent(message.getContent());
		responseMessage.setType("update");
		
		return responseMessage;
	}


	@Override
	public MessageDTO deleteChatMessage(MessageDTO message) {
		String content = message.getContent();
		MessageDTO responseMessage = new MessageDTO();
		responseMessage.setSenderNo(message.getSenderNo());
		
		
		Team team = Team.builder()
						.userNo(message.getSenderNo())
						.teamId(message.getTeamId())
						.build();
		
		if(teamMapper.checkTeam(message.getTeamId())) {
			responseMessage.setType("요청 보낸 팀이 존재하지 않습니다.");
			return responseMessage;
		}
		
		if(teamMapper.findMemberByUserNo(team)) {
			responseMessage.setType("요청 보낸 사용자는 팀원이 아닙니다.");
			return responseMessage;
		}
		
		if(chatMapper.checkIsSender(message)) {
			responseMessage.setType("발신자가 아니면 삭제가 불가 합니다.");
			return responseMessage;
		}
		
		int deleteChatMessage = chatMapper.deleteChatMessage(message.getMessageId());
		
		if(deleteChatMessage == 0) {
			responseMessage.setType("메시지 삭제에 실패했습니다.");
			return responseMessage;
		}
		
		
		responseMessage.setMessageId(message.getMessageId());
		responseMessage.setType("delete");
		
		return responseMessage;
	}

}
