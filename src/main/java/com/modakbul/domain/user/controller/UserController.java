package com.modakbul.domain.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.board.dto.BoardInfoDto;
import com.modakbul.domain.user.dto.BlockListResDto;
import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyMatchesRequestHistoryDto;
import com.modakbul.domain.user.dto.MyProfileReqDto;
import com.modakbul.domain.user.dto.MyProfileResDto;
import com.modakbul.domain.user.dto.ReportListResDto;
import com.modakbul.domain.user.dto.UserCafeResDto;
import com.modakbul.domain.user.dto.UserProfileResDto;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.service.UserService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/users/mypage/profile")
	public BaseResponse<MyProfileResDto> getMyProfileDetails(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_PROFILE_SUCCESS, userService.getMyProfileDetails(user));
	}

	@PatchMapping("/users/profile")
	public BaseResponse<Void> updateMyProfile(@AuthenticationPrincipal User user,
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "user") MyProfileReqDto request) {
		userService.updateMyProfile(user, image, request);
		return new BaseResponse<>(BaseResponseStatus.UPDATE_PROFILE_SUCCESS);
	}

	@GetMapping("/users/cafes")
	public BaseResponse<List<UserCafeResDto>> getCafesHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_USER_CAFE_SUCCESS, userService.getCafesHistory(user));
	}

	@GetMapping("/users/meetings")
	public BaseResponse<List<MeetingsHistoryResDto>> getMeetingsHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_MEETINGS_HISTORY_SUCCESS,
			userService.getMeetingsHistory(user));
	}

	@GetMapping("/users/boards")
	public BaseResponse<List<BoardInfoDto>> getMyBoardHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_MY_BOARD_HISTORY_SUCCESS, userService.getMyBoardHistory(user));
	}

	@GetMapping("/users/matches/requests")
	public BaseResponse<List<MyMatchesRequestHistoryDto>> getMyMatchesRequestHistory(
		@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_MATCHES_REQUEST_HISTORY_SUCCESS,
			userService.getMyMatchesRequestHistory(user));
	}

	@GetMapping("/users/profile/{userId}")
	public BaseResponse<UserProfileResDto> getUserProfile(@PathVariable Long userId) {
		return new BaseResponse<>(BaseResponseStatus.GET_USER_PROFILE_SUCCESS, userService.getUserProfile(userId));
	}

	@GetMapping("/users/blocks")
	public BaseResponse<List<BlockListResDto>> getBlockedUserList(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_BLOCK_LIST_SUCCESS, userService.getBlockedUserList(user));
	}

	@GetMapping("/users/reports")
	public BaseResponse<List<ReportListResDto>> getReportedUserList(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_REPORT_LIST_SUCCESS, userService.getReportedUserList(user));
	}

}
