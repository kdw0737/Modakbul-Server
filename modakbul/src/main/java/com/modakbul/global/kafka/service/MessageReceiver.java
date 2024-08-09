package com.modakbul.global.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.modakbul.domain.chat.chatmessage.dto.MessageDto;
import com.modakbul.global.kafka.util.KafkaUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiver {

	private final SimpMessageSendingOperations template;

	@KafkaListener(topics = KafkaUtil.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
	public void receiveMessage(MessageDto message) {
		log.info("전송 위치 = /sub/chat/" + message.getChatRoomId());
		log.info("채팅 방으로 메시지 전송 = {}", message);

		// 메시지객체 내부의 채팅방번호를 참조하여, 해당 채팅방 구독자에게 메시지를 발송한다.
		template.convertAndSend("/sub/chat/" + message.getChatRoomId(), message);
	}
}
