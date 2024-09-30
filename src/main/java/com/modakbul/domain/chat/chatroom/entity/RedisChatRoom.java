package com.modakbul.domain.chat.chatroom.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "chatRoom")
public class RedisChatRoom {
	@Id
	private String id;

	@Indexed
	private Long chatRoomId;

	private Set<String> connectedUsers = new HashSet<>();
}
