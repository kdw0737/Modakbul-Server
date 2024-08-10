package com.modakbul.domain.chat.chatroom.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.modakbul.domain.chat.chatroom.entity.RedisChatRoom;

public interface ConnectedChatUserRepository extends CrudRepository<RedisChatRoom, String> {
	Optional<RedisChatRoom> findByChatRoomId(Long chatRoomId);
}
