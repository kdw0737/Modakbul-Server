package com.modakbul.domain.chat.chatmessage.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Document(collection = "chatMessage")
public class ChatMessage {
	@Id
	private String id;

	private Long userId;

	private Long chatRoomId;

	private String content; // 메세지 내용

	private LocalDateTime sendDate; // 보낸 시간

	private Integer readCount; // 읽은 수
}
