package com.modakbul.domain.chat.chatroom.entity;

import java.util.ArrayList;
import java.util.List;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.chat.chatroom.enums.ChatRoomType;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class ChatRoom extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	private int roomHashCode; // 단체채팅의 경우 0, 일대일 채팅에 사용

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
	private List<UserChatRoom> chatRoomUsers = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private ChatRoomType chatRoomType; // GROUP, ONE_TO_ONE

	public void addChatUser(UserChatRoom chatRoomUser) {
		this.getChatRoomUsers().add(chatRoomUser);
	}

	public void setChatRoomUsers() {
		this.chatRoomUsers = new ArrayList<>();
	}
}
