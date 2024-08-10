package com.modakbul.domain.user.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyBoardHistoryResDto;
import com.modakbul.domain.user.dto.UserRequestDto;
import com.modakbul.domain.user.dto.UserResponseDto;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.domain.user.service.UserService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	@GetMapping("/users/mypage/profile")
	public BaseResponse<UserResponseDto.ProfileDto> profileDetails(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_PROFILE_SUCCESS, userService.findProfile(user));
	}

	@PatchMapping("/users/profile")
	public BaseResponse<Void> profileModify(@AuthenticationPrincipal User user,
		@RequestBody UserRequestDto.ProfileDto request) {
		userService.modifyProfile(user, request);
		return new BaseResponse<>(BaseResponseStatus.UPDATE_PROFILE_SUCCESS);
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
