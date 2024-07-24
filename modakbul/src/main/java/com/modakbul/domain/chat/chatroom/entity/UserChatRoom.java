package com.modakbul.domain.chat.chatroom.entity;

import java.time.LocalDateTime;

import com.modakbul.domain.chat.chatroom.enums.UserChatRoomStatus;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class UserChatRoom extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "user_chat_room_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_rooom_id")
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	private UserChatRoomStatus userChatRoomStatus; // ACTIVE, INACTIVE ( 사용자가 방에 있는 상태, 방을 떠난 상태 )

	private LocalDateTime lastExitedAt; // 마지막에 방을 나간 시간
}
