package com.modakbul.domain.report.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.report.dto.ReportReqDto;
import com.modakbul.domain.report.service.ReportService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/reports/{reportedId}")
	public BaseResponse<Void> reportUserProfile(@AuthenticationPrincipal User user, @PathVariable Long reportedId,
		@RequestBody ReportReqDto reportReqDto) {
		reportService.reportUserProfile(user, reportedId, reportReqDto);
		return new BaseResponse<>(BaseResponseStatus.REPORT_PROFILE_SUCCESS);
	}

	@PostMapping("/reports/{chatroomId}/{reportedId}")
	public BaseResponse<Void> reportChatroom(@AuthenticationPrincipal User user, @PathVariable Long chatroomId,
		@PathVariable Long reportedId, @RequestBody ReportReqDto reportReqDto) {
		reportService.reportChatroom(user, chatroomId, reportedId, reportReqDto);
		return new BaseResponse<>(BaseResponseStatus.REPORT_CHATROOM_SUCCESS);
	}

}
