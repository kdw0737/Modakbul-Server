package com.modakbul.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.auth.dto.CheckNicknameDto;
import com.modakbul.domain.auth.service.AuthService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@DeleteMapping("/users/logout")
	public BaseResponse<Void> logout(@RequestHeader("Authorization") String accessToken,
		@AuthenticationPrincipal User user) {
		authService.logout(accessToken, user);
		return new BaseResponse<>(BaseResponseStatus.LOGOUT_SUCCESS);
	}

	@PostMapping("/token/reissue")
	public ResponseEntity<BaseResponse> reissue(@RequestHeader("Authorization_refresh") String refreshToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(authService.reissue(refreshToken));

		return new ResponseEntity(new BaseResponse<>(BaseResponseStatus.REISSUE_TOKEN_SUCCESS), httpHeaders,
			HttpStatus.OK);
	}

	@GetMapping("/users")
	public BaseResponse<CheckNicknameDto> checkNickname(@RequestParam String nickname) {
		CheckNicknameDto checkNickname = authService.checkNickname(nickname);

		if (checkNickname.isAbuse()) {
			return new BaseResponse<>(BaseResponseStatus.NICKNAME_ABUSE, checkNickname);
		} else if (checkNickname.isOverlapped()) {
			return new BaseResponse<>(BaseResponseStatus.NICKNAME_DUPLICATED, checkNickname);
		} else {
			return new BaseResponse<>(BaseResponseStatus.CHECK_NICKNAME_SUCCESS, checkNickname);
		}
	}
}
