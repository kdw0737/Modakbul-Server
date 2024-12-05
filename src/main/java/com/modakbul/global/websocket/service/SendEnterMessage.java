package com.modakbul.global.websocket.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.modakbul.domain.chat.chatmessage.dto.EnterMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEnterMessage {

	private final SimpMessageSendingOperations messagingTemplate;

	public void sendEnterMessage(EnterMessageDto enterMessageDto) {
		// WebSocket으로 메시지 전송
		messagingTemplate.convertAndSend("/sub/chat/" + enterMessageDto.getChatRoomId(), enterMessageDto);
	}
}
