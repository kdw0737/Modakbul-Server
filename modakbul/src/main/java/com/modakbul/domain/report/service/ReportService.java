package com.modakbul.domain.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.chat.chatroom.entity.ChatRoom;
import com.modakbul.domain.chat.chatroom.entity.UserChatRoom;
import com.modakbul.domain.chat.chatroom.repository.ChatRoomRepository;
import com.modakbul.domain.chat.chatroom.repository.UserChatRoomRepository;
import com.modakbul.domain.report.dto.ReportReqDto;
import com.modakbul.domain.report.entity.ChatReport;
import com.modakbul.domain.report.entity.UserReport;
import com.modakbul.domain.report.repository.ChatReportRepository;
import com.modakbul.domain.report.repository.UserReportRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final UserReportRepository userReportRepository;
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatReportRepository chatReportRepository;
	private final UserChatRoomRepository userChatRoomRepository;

	@Transactional
	public void reportUserProfile(User user, Long reportedId, ReportReqDto reportReqDto) {
		User findReported = userRepository.findById(reportedId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		UserReport userReport = UserReport.builder()
			.reporter(user)
			.reported(findReported)
			.content(reportReqDto.getContent())
			.build();

		userReportRepository.save(userReport);
	}

	@Transactional
	public void reportChatroom(User user, Long chatroomId, Long reportedId, ReportReqDto reportReqDto) {
		User findReported = userRepository.findById(reportedId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		ChatRoom findChatRoom = chatRoomRepository.findById(chatroomId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

		UserChatRoom findUserChatroom = userChatRoomRepository.findByUserId(user.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

		findUserChatroom.inActive();

		ChatReport chatReport = ChatReport.builder()
			.reporter(user)
			.reported(findReported)
			.chatRoom(findChatRoom)
			.content(reportReqDto.getContent())
			.build();

		chatReportRepository.save(chatReport);

	}
}
