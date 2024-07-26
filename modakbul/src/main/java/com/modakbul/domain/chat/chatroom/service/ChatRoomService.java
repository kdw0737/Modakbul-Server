package com.modakbul.domain.chat.chatroom.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.chat.chatroom.dto.CreateOneToOneChatReq;
import com.modakbul.domain.chat.chatroom.entity.ChatRoom;
import com.modakbul.domain.chat.chatroom.entity.UserChatRoom;
import com.modakbul.domain.chat.chatroom.enums.ChatRoomType;
import com.modakbul.domain.chat.chatroom.enums.UserChatRoomStatus;
import com.modakbul.domain.chat.chatroom.repository.ChatRoomRepository;
import com.modakbul.domain.chat.chatroom.repository.UserChatRoomRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.enums.UserStatus;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final UserChatRoomRepository userChatRoomRepository;

	@Transactional
	public Long createOneToOneChatRoom(CreateOneToOneChatReq createOneToOneChatReq, User user) {
		// 상대방 조회
		User findTheOtherUser = userRepository.findByIdAndUserStatus(createOneToOneChatReq.getTheOtherUserId(),
				UserStatus.ACTIVE)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.ID_NOT_EXIST));

		// roomHashCode 생성
		int roomHashCode = createRoomHashCode(user, findTheOtherUser);

		// 채팅방 조회
		ChatRoom findChatRoom = chatRoomRepository.findByRoomHashCode(roomHashCode).orElse(null);

		if (findChatRoom != null) {
			UserChatRoom findUserChatRoom = userChatRoomRepository.findByUserId(user.getId())
				.orElseThrow(() -> new BaseException(BaseResponseStatus.ID_NOT_EXIST));

			// 대화방을 나간 상태였으면 status 변경
			findUserChatRoom.active();

			return findChatRoom.getId();
		} else {
			// 게시글 조회
			Board findBoard = boardRepository.findById(createOneToOneChatReq.getBoardId())
				.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

			// 채팅방 생성
			ChatRoom chatRoom = ChatRoom.builder()
				.board(findBoard)
				.roomHashCode(roomHashCode)
				.userCount(2)
				.chatRoomType(ChatRoomType.ONE_TO_ONE)
				.build();

			UserChatRoom userChatRoom = UserChatRoom.builder()
				.user(user)
				.chatRoom(chatRoom)
				.userChatRoomStatus(UserChatRoomStatus.ACTIVE)
				.lastExitedAt(LocalDateTime.now())
				.build();

			UserChatRoom theOtherUserChatRoom = UserChatRoom.builder()
				.user(findTheOtherUser)
				.chatRoom(chatRoom)
				.userChatRoomStatus(UserChatRoomStatus.ACTIVE)
				.lastExitedAt(LocalDateTime.now())
				.build();

			chatRoomRepository.save(chatRoom);
			userChatRoomRepository.save(userChatRoom);
			userChatRoomRepository.save(theOtherUserChatRoom);

			return chatRoom.getId();
		}
	}

	@Transactional
	public void exitChatRoom(Long chatRoomId, User user) {
		UserChatRoom findUserChatRoom = userChatRoomRepository.findByUserId(user.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.ID_NOT_EXIST));

		findUserChatRoom.inActive();
	}

	// 채팅방 해시코드 생성
	private int createRoomHashCode(User user, User anotherUser) {
		Long userId = user.getId();
		Long anotherId = anotherUser.getId();
		return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
	}

}
