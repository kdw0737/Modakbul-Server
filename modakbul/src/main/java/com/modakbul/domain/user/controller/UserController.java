package com.modakbul.domain.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyBoardHistoryResDto;
import com.modakbul.domain.user.dto.UserReqDto;
import com.modakbul.domain.user.dto.UserResDto;
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
	public BaseResponse<UserResDto.ProfileDto> getProfileDetails(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_PROFILE_SUCCESS, userService.getProfileDetails(user));
	}

	@PatchMapping("/users/profile")
	public BaseResponse<Void> updateProfile(@AuthenticationPrincipal User user,
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "user") UserReqDto.ProfileDto request) {
		userService.updateProfile(user, image, request);
		return new BaseResponse<>(BaseResponseStatus.UPDATE_PROFILE_SUCCESS);
	}

	@GetMapping("/users/cafes")
	public BaseResponse<List<UserResDto.UserCafeDto>> getCafesHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_USER_CAFE_SUCCESS, userService.getCafesHistory(user));
	}

	@GetMapping("/users/meetings")
	public BaseResponse<List<MeetingsHistoryResDto>> getMeetingsHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_MATCHES_HISTORY_SUCCESS, userService.getMeetingsHistory(user));
	}

	@GetMapping("/users/boards")
	public BaseResponse<List<MyBoardHistoryResDto>> getMyBoardHistory(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_MY_BOARD_HISTORY_SUCCESS, userService.getMyBoardHistory(user));
	}
}
