package com.modakbul.global.websocket.util;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
	//
	// private final JwtService jwtService;
	// private final ChatRoomService chatRoomService;
	// private final ChatService chatService;
	//
	// @Override
	// public Message<?> preSend(Message<?> message, MessageChannel channel) {
	// 	StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
	// 	// StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
	// 	String accessToken = getAccessToken(accessor);
	// 	if (accessToken == null) {
	// 		throw new BaseException(BaseResponseStatus.TOKEN_NOT_FOUND);
	// 	}
	// 	String nickname = verifyAccessToken(accessToken);
	// 	log.info("StompAccessor = {}", accessor);
	// 	handleMessage(accessor.getCommand(), accessor, nickname);
	// 	return message;
	// }
	//
	// private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String nickname) {
	// 	switch (stompCommand) {
	// 		case CONNECT:
	// 			connectToChatRoom(accessor, nickname);
	// 			break;
	// 		//            case SUBSCRIBE:
	// 		//            case SEND:
	// 		//                tokenHandler.getUid(getAccessToken(accessor));
	// 		//                break;
	// 		case DISCONNECT:
	// 			chatRoomService.disconnectChatRoom(getChatRoomNo(accessor), nickname);
	// 			break;
	// 	}
	// }
	//
	// private void connectToChatRoom(StompHeaderAccessor accessor, String nickname) {
	// 	// 채팅방 번호를 가져온다.
	// 	Long chatRoomId = getChatRoomId(accessor);
	// 	// 채팅방 입장 처리 -> Redis에 입장 내역 저장
	// 	chatRoomService.connectChatRoom(chatRoomId, nickname);
	// 	// 읽지 않은 채팅을 전부 읽음 처리
	// 	chatService.updateCountAllZero(chatRoomId, nickname);
	// 	// 현재 채팅방에 접속중인 인원이 있는지 확인한다.
	// 	boolean isConnected = chatRoomService.isConnected(chatRoomId);
	//
	// 	if (isConnected) {
	// 		chatService.updateMessage(nickname, chatRoomId);
	// 	}
	// }
	//
	// private String getAccessToken(StompHeaderAccessor accessor) {
	// 	return accessor.getFirstNativeHeader("Authorization");
	// }
	//
	// private String verifyAccessToken(String accessToken) {
	// 	return tokenHandler.getUserNickname(accessToken);
	// }
	//
	// private Long getChatRoomId(StompHeaderAccessor accessor) {
	// 	return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomId")));
	// }
}
