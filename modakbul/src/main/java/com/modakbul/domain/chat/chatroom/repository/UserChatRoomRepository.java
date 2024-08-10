package com.modakbul.domain.chat.chatroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.domain.chat.chatroom.entity.UserChatRoom;
import com.modakbul.domain.chat.chatroom.enums.UserChatRoomStatus;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
	Optional<UserChatRoom> findByUserId(Long userId);

	List<UserChatRoom> findAllByUserIdAndUserChatRoomStatus(Long userId, UserChatRoomStatus status);

	Optional<UserChatRoom> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
