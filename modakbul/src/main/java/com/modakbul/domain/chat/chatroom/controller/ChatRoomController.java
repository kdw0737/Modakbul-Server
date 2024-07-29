package com.modakbul.domain.chat.chatroom.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.chat.chatroom.dto.CreateOneToOneChatReq;
import com.modakbul.domain.chat.chatroom.service.ChatRoomService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	// 채팅방 생성
	@PostMapping("/chatrooms")
	public BaseResponse<Long> createOneToOneChatRoom(
		@RequestBody CreateOneToOneChatReq createOneToOneChatReq,
		@AuthenticationPrincipal User user
	) {
		// 상대방 ID 와 내 ID가 같은 경우 오류
		if (createOneToOneChatReq.getTheOtherUserId() == user.getId())
			throw new BaseException(BaseResponseStatus.USER_CANNOT_MAKE_CHATROOM_ALONE);

		Long chatRoomId = chatRoomService.createOneToOneChatRoom(createOneToOneChatReq, user);

		return new BaseResponse<>(BaseResponseStatus.CREATE_CHATROOM_SUCCESS, chatRoomId);
	}

	@PatchMapping("/chatrooms/{chatRoomId}")
	public BaseResponse<Void> exitChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal User user) {
		chatRoomService.exitChatRoom(chatRoomId, user);

		return new BaseResponse<>(BaseResponseStatus.EXIT_CHATROOM_SUCCESS);
	}
}
