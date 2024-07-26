package com.modakbul.domain.chatMessage.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.modakbul.domain.chat.chatmessage.entity.ChatMessage;
import com.modakbul.domain.chat.chatmessage.enums.MessageType;
import com.modakbul.domain.chat.chatmessage.repository.ChatMessageRepository;

@DataMongoTest
public class ChatMessageServiceTest {

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Test
	public void testInsertAndFindChatMessage() {
		// Given
		ChatMessage chatMessage = ChatMessage.builder()
			.userId(1L)
			.chatRoomId(1L)
			.message("Hello, World!")
			.messageType(MessageType.TALK)
			.build();

		// When
		ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

		// Then
		Optional<ChatMessage> retrievedChatMessage = chatMessageRepository.findById(savedChatMessage.getId());
		assertThat(retrievedChatMessage).isPresent();
		assertThat(retrievedChatMessage.get().getMessage()).isEqualTo("Hello, World!");
		System.out.println(
			"retrievedChatMessage.get().getChatRoomId() = " + retrievedChatMessage.get().getChatRoomId());
		System.out.println("retrievedChatMessage.get().getMessage() = " + retrievedChatMessage.get().getMessage());
	}
}
