package com.modakbul.global.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.modakbul.domain.chat.chatmessage.dto.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {

	private final KafkaTemplate<String, MessageDto> kafkaTemplate;

	// 메시지를 지정한 Kafka 토픽으로 전송
	public void send(String topic, MessageDto data) {

		// KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
		kafkaTemplate.send(topic, data);
	}
}
