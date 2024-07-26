package com.modakbul.domain.chat.chatroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.domain.chat.chatroom.entity.UserChatRoom;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
	Optional<UserChatRoom> findByUserId(Long userId);
}
