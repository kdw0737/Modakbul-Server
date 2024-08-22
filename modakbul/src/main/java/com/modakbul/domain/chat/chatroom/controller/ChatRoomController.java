package com.modakbul.domain.chat.chatroom.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.chat.chatroom.dto.CreateOneToOneChatReqDto;
import com.modakbul.domain.chat.chatroom.dto.GetMessageHistoryResDto;
import com.modakbul.domain.chat.chatroom.dto.GetOneToOneChatRoomListResDto;
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
		@RequestBody CreateOneToOneChatReqDto createOneToOneChatReqDto,
		@AuthenticationPrincipal User user
	) {
		// 상대방 ID 와 내 ID가 같은 경우 오류
		if (createOneToOneChatReqDto.getTheOtherUserId() == user.getId())
			throw new BaseException(BaseResponseStatus.USER_CANNOT_MAKE_CHATROOM_ALONE);
	
		Long chatRoomId = chatRoomService.createOneToOneChatRoom(createOneToOneChatReqDto, user);

		return new BaseResponse<>(BaseResponseStatus.CREATE_CHATROOM_SUCCESS, chatRoomId);
	}

	@PatchMapping("/chatrooms/{chatRoomId}")
	public BaseResponse<Void> exitChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal User user) {
		chatRoomService.exitChatRoom(chatRoomId, user);

		return new BaseResponse<>(BaseResponseStatus.EXIT_CHATROOM_SUCCESS);
	}

	@GetMapping("/chatrooms")
	public BaseResponse<List<GetOneToOneChatRoomListResDto>> getOneToOneChatRoomList(
		@AuthenticationPrincipal User user) {
		List<GetOneToOneChatRoomListResDto> chatRoomList = chatRoomService.getOneToOneChatRoomList(user);

		return new BaseResponse<>(BaseResponseStatus.GET_CHATROOM_LIST_SUCCESS, chatRoomList);
	}

	@GetMapping("/chatrooms/{chatroomId}/{boardId}") //TODO: 채팅방에 접속해 있는 경우 상대방이 들어와서 읽으면 내 화면에도 동기화 필요
	public BaseResponse<GetMessageHistoryResDto> getMessageHistory(@AuthenticationPrincipal User user,
		@PathVariable Long chatroomId,
		@PathVariable Long boardId) {
		GetMessageHistoryResDto messageHistory = chatRoomService.getMessageHistory(user, chatroomId, boardId);
		return new BaseResponse<>(BaseResponseStatus.GET_NEW_MESSAGE_SUCCESS, messageHistory);
	}
}
