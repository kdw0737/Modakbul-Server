package com.modakbul.domain.chat.chatmessage.repository;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.chat.chatmessage.entity.ChatMessage;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {
	private final MongoTemplate mongoTemplate;

	@Override
	public void updateReadCount(Long chatRoomId, Long userId) {
		Update update = new Update().inc("readCount", -1);
		//ne-> not equal
		Query query = new Query(where("chatRoomId").is(chatRoomId)
			.and("userId").ne(userId));

		mongoTemplate.updateMulti(query, update, ChatMessage.class);
	}
}
