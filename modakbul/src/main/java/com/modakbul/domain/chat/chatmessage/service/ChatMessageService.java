package com.modakbul.domain.chat.chatmessage.service;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.chat.chatmessage.dto.MessageDto;
import com.modakbul.domain.chat.chatmessage.entity.ChatMessage;
import com.modakbul.domain.chat.chatmessage.repository.ChatMessageRepository;
import com.modakbul.domain.chat.chatroom.repository.ChatRoomRepository;
import com.modakbul.domain.chat.chatroom.repository.UserChatRoomRepository;
import com.modakbul.domain.chat.chatroom.service.ChatRoomService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.kafka.service.MessageSender;
import com.modakbul.global.kafka.util.KafkaUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final MessageSender messageSender;
	private final ChatRoomService chatRoomService;

	public void sendMessage(MessageDto messageDto, User user) { //TODO : 빈 방이 열리면 상대방은 모르다가 메세지 전송 시 상대방한테 채팅방 생성
		Integer readCount = chatRoomService.checkConnectedUser(messageDto.getChatRoomId());

		ChatMessage chatMessage = ChatMessage.builder()
			.id(ObjectId.get().toHexString())
			.sendDate(LocalDateTime.now())
			.userId(user.getId())
			.chatRoomId(messageDto.getChatRoomId())
			.content(messageDto.getContent())
			.readCount(readCount)
			.build();

		chatMessageRepository.save(chatMessage);

		messageDto.setReadCount(readCount);

		messageSender.send(KafkaUtil.KAFKA_TOPIC, messageDto);
	}
}
