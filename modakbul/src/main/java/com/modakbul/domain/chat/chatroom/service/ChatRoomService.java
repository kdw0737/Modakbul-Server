package com.modakbul.domain.chat.chatroom.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.cafe.repository.CafeRepository;
import com.modakbul.domain.chat.chatmessage.entity.ChatMessage;
import com.modakbul.domain.chat.chatmessage.repository.ChatMessageRepository;
import com.modakbul.domain.chat.chatmessage.repository.CustomChatMessageRepository;
import com.modakbul.domain.chat.chatroom.dto.CreateOneToOneChatReq;
import com.modakbul.domain.chat.chatroom.dto.GetMessageHistoryRes;
import com.modakbul.domain.chat.chatroom.dto.GetOneToOneChatRoomListRes;
import com.modakbul.domain.chat.chatroom.entity.ChatRoom;
import com.modakbul.domain.chat.chatroom.entity.RedisChatRoom;
import com.modakbul.domain.chat.chatroom.entity.UserChatRoom;
import com.modakbul.domain.chat.chatroom.enums.ChatRoomType;
import com.modakbul.domain.chat.chatroom.enums.UserChatRoomStatus;
import com.modakbul.domain.chat.chatroom.repository.ChatRoomRepository;
import com.modakbul.domain.chat.chatroom.repository.ConnectedChatUserRepository;
import com.modakbul.domain.chat.chatroom.repository.UserChatRoomRepository;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.enums.UserStatus;
import com.modakbul.domain.user.repository.CategoryRepository;
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
	private final ConnectedChatUserRepository connectedChatUserRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final CustomChatMessageRepository customChatMessageRepository;
	private final CategoryRepository categoryRepository;
	private final CafeRepository cafeRepository;

	@Transactional
	public Long createOneToOneChatRoom(CreateOneToOneChatReq createOneToOneChatReq, User user) {
		// 상대방 조회
		User findTheOtherUser = userRepository.findByIdAndUserStatus(createOneToOneChatReq.getTheOtherUserId(),
				UserStatus.ACTIVE)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		// roomHashCode 생성
		int roomHashCode = createRoomHashCode(user, findTheOtherUser);

		// 채팅방 조회
		ChatRoom findChatRoom = chatRoomRepository.findByRoomHashCode(roomHashCode).orElse(null);

		if (findChatRoom != null) {
			UserChatRoom findUserChatRoom = userChatRoomRepository.findByUserId(user.getId())
				.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

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
				.userChatRoomStatus(UserChatRoomStatus.ACTIVE) //TODO: 비활성화 했다가 메세지를 받는 경우에 활성화
				.lastExitedAt(LocalDateTime.now())
				.build();

			chatRoom.getChatRoomUsers().add(userChatRoom);
			chatRoom.getChatRoomUsers().add(theOtherUserChatRoom);

			chatRoomRepository.save(chatRoom);
			userChatRoomRepository.save(userChatRoom);
			userChatRoomRepository.save(theOtherUserChatRoom);

			return chatRoom.getId();
		}
	}

	@Transactional
	public void connectChatRoom(Long chatRoomId, String nickname) {
		RedisChatRoom redisChatRoom = connectedChatUserRepository.findByChatRoomId(chatRoomId)
			.orElseGet(() -> {
				RedisChatRoom newChatRoom = RedisChatRoom.builder()
					.id(UUID.randomUUID().toString())
					.chatRoomId(chatRoomId)
					.connectedUsers(new HashSet<>())
					.build();
				return newChatRoom;
			});

		if (!redisChatRoom.getConnectedUsers().contains(nickname)) {
			redisChatRoom.getConnectedUsers().add(nickname);
			connectedChatUserRepository.save(redisChatRoom);
		}
	}

	@Transactional
	public void disconnectChatRoom(Long chatRoomId, String nickname) {
		RedisChatRoom findRedisChatRoom = connectedChatUserRepository.findByChatRoomId(chatRoomId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

		findRedisChatRoom.getConnectedUsers().remove(nickname);
		if (findRedisChatRoom.getConnectedUsers().isEmpty()) {
			connectedChatUserRepository.delete(findRedisChatRoom);
		} else {
			connectedChatUserRepository.save(findRedisChatRoom);
		}
	}

	@Transactional
	public List<GetOneToOneChatRoomListRes> getOneToOneChatRoomList(User user) {
		List<UserChatRoom> findChatRoomList = userChatRoomRepository.findAllByUserIdAndUserChatRoomStatus(user.getId(),
			UserChatRoomStatus.ACTIVE);

		if (findChatRoomList.isEmpty()) {
			return Collections.emptyList();
		}

		return findChatRoomList.stream().map(userChatRoom -> {
			ChatRoom findChatRoom = chatRoomRepository.findById(userChatRoom.getChatRoom().getId())
				.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

			Long theOtherUserId = findChatRoom.getChatRoomUsers().get(0).getId().equals(user.getId()) ?
				findChatRoom.getChatRoomUsers().get(1).getId() : user.getId();

			User findTheOtherUser = userRepository.findByIdAndUserStatus(theOtherUserId, UserStatus.ACTIVE)
				.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

			Page<ChatMessage> findMessage = chatMessageRepository.findByChatRoomIdOrderBySendDateDesc(
				findChatRoom.getId(), PageRequest.of(0, 1));

			ChatMessage lastMessage = findMessage.hasContent() ? findMessage.getContent().get(0) : null;

			Integer unReadCount = chatMessageRepository.countUnreadMessages(findChatRoom.getId(), user.getId());

			return GetOneToOneChatRoomListRes.builder()
				.roomTitle(findTheOtherUser.getNickname())
				.chatRoomId(findChatRoom.getId())
				.boardId(findChatRoom.getBoard().getId())
				.theOtherUserId(theOtherUserId)
				.theOtherUserImage(findTheOtherUser.getImage())
				.lastMessage(lastMessage.getContent())
				.lastMessageTime(lastMessage.getSendDate())
				.unreadCount(unReadCount)
				.build();
		}).collect(Collectors.toList());
	}

	@Transactional
	public void exitChatRoom(Long chatRoomId, User user) {
		UserChatRoom findUserChatRoom = userChatRoomRepository.findByUserId(user.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		findUserChatRoom.inActive();
	}

	// 채팅방 접속중인 인원 확인
	public Integer checkConnectedUser(Long chatRoomId) {
		RedisChatRoom findRedisChatRoom = connectedChatUserRepository.findByChatRoomId(chatRoomId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

		ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

		return findChatRoom.getChatRoomUsers().size() - findRedisChatRoom.getConnectedUsers().size();
	}

	public GetMessageHistoryRes getMessageHistory(User user, Long chatRoomId, Long boardId) {
		List<ChatMessage> findUnreadMessages = chatMessageRepository.findByChatRoomIdAndUserIdNotAndReadCountOrderBySendDateAsc(
			chatRoomId,
			user.getId(), 1);

		// 메시지 내용과 보낸 시간을 리스트로 변환합니다.
		List<String> contents = findUnreadMessages.stream()
			.map(ChatMessage::getContent)
			.toList();

		List<LocalDateTime> sendTimes = findUnreadMessages.stream()
			.map(ChatMessage::getSendDate)
			.toList();

		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		Category findCategory = categoryRepository.findById(findBoard.getCategory().getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CATEGORY_NOT_FOUND));

		Cafe findCafe = cafeRepository.findById(findBoard.getCafe().getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

		return new GetMessageHistoryRes().builder()
			.contents(contents)
			.sendTimes(sendTimes)
			.cafeName(findCafe.getName())
			.boardTitle(findBoard.getTitle())
			.categoryName(findCategory.getCategoryName())
			.build();
	}

	//메세지 읽음 처리

	public void updateReadCount(Long chatRoomId, Long userId) {
		customChatMessageRepository.updateReadCount(chatRoomId, userId);
	}

	// 채팅방 해시코드 생성
	private int createRoomHashCode(User user, User anotherUser) {
		Long userId = user.getId();
		Long anotherId = anotherUser.getId();
		return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
	}
}
