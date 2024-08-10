package com.modakbul.domain.chat.chatmessage.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.chat.chatmessage.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	Page<ChatMessage> findByChatRoomIdOrderBySendDateDesc(Long chatRoomId, Pageable pageable);

	// 1대1 채팅방에서 읽지 않은 메시지 수를 세는 쿼리 메서드
	@Query("{ 'chatRoomId': ?0, 'readCount': 1, 'senderId': { $ne: ?1 } }")
	Integer countUnreadMessages(Long chatRoomId, Long userId);

	// 1대1 채팅방에서 읽지 않은 메시지들을 시간순서대로 정렬하여 조회
	List<ChatMessage> findByChatRoomIdAndUserIdNotAndReadCountOrderBySendDateAsc(Long chatRoomId, Long userId,
		int readCount);
}
