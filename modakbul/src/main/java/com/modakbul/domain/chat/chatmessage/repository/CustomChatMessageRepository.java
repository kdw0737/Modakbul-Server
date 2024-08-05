package com.modakbul.domain.chat.chatmessage.repository;

public interface CustomChatMessageRepository {
	void updateReadCount(Long chatRoomId, Long userId);
}
