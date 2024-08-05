package com.modakbul.global.websocket.util;

import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.modakbul.domain.chat.chatroom.service.ChatRoomService;
import com.modakbul.global.auth.jwt.JwtProvider;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JwtProvider jwtProvider;
	private final ChatRoomService chatRoomService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
		String accessToken = getAccessToken(accessor);
		if (accessToken == null) {
			throw new BaseException(BaseResponseStatus.TOKEN_NOT_FOUND);
		}
		String nickname = verifyAccessToken(accessToken);
		log.info("StompAccessor = {}", accessor);
		handleMessage(accessor.getCommand(), accessor, nickname);
		return message;
	}

	private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String nickname) {
		switch (stompCommand) {
			case CONNECT:
				connectToChatRoom(accessor, nickname);
				break;
			case DISCONNECT:
				chatRoomService.disconnectChatRoom(getChatRoomId(accessor), nickname);
				break;
		}
	}

	private void connectToChatRoom(StompHeaderAccessor accessor, String nickname) {
		// 채팅방 번호를 가져온다.
		Long chatRoomId = getChatRoomId(accessor);
		// 채팅방 입장 처리 -> Redis에 입장 내역 저장
		chatRoomService.connectChatRoom(chatRoomId, nickname);
		// 읽지 않은 채팅을 전부 읽음 처리
		chatRoomService.updateReadCount(chatRoomId, getUserId(accessor));
	}

	private String getAccessToken(StompHeaderAccessor accessor) {
		return accessor.getFirstNativeHeader("Authorization");
	}

	private String verifyAccessToken(String accessToken) {
		return jwtProvider.getNickName(accessToken);
	}

	private Long getChatRoomId(StompHeaderAccessor accessor) {
		return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomId")));
	}

	private Long getUserId(StompHeaderAccessor accessor) {
		return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("userId")));
	}
}
