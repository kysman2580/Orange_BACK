package com.kh.dotogether.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.dotogether.chat.model.vo.GetMessagesRequest;
import com.kh.dotogether.chat.model.vo.Message;
import com.kh.dotogether.chat.model.dto.MessageDTO;

@Mapper
public interface ChatMapper {
	
	/**
     * 팀 채팅방의 메시지 목록 조회 (커서 기반 페이징)
     *
     * @param request teamId 및 마지막 메시지 시간 정보를 담은 요청 객체
     * @return 조회된 메시지 DTO 리스트 (최대 20개)
     */
	List<MessageDTO> findMessagesByRoomId(GetMessagesRequest request);
	
	
	/**
     * 새로운 채팅 메시지를 DB에 저장
     *
     * @param message 저장할 메시지 객체
     * @return 저장된 행 수 (성공 시 1)
     */
	int sendChatMessage(Message message);
	
	
	/**
     * 메시지 ID로 단일 메시지 상세 조회
     *
     * @param messageId 조회할 메시지의 고유 ID
     * @return 조회된 메시지 DTO (존재하지 않으면 null)
     */
	MessageDTO findMessageByMessageId(String messageId);
	
	
	/**
     * 메시지 존재 여부 확인
     *
     * @param messageId 확인할 메시지 ID
     * @return true = 메시지 존재하지 않음, false = 존재함
     */
	boolean checkMessage(String messageId);
	
	
	/**
     * 해당 메시지의 발신자인지 확인
     *
     * @param message 메시지 ID와 발신자 정보가 포함된 DTO
     * @return true = 발신자가 아님, false = 발신자 본인
     */
	boolean checkIsSender(MessageDTO message);
	
	
	/**
     * 채팅 메시지 내용 수정
     *
     * @param message 수정할 메시지의 ID 및 수정 내용 포함 DTO
     * @return 수정된 행 수 (성공 시 1)
     */
	int updateChatMessage(MessageDTO message);
	
	
	/**
     * 메시지 삭제 처리
     *
     * @param messageId 삭제할 메시지의 ID
     * @return 삭제된 행 수 (성공 시 1)
     */
	int deleteChatMessage(String messageId);
}
