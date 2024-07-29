package com.modakbul.domain.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.auth.dto.AuthRequestDto;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.service.AuthService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/users/login")
	public BaseResponse<HttpHeaders> login(@RequestBody AuthRequestDto.loginDto request) {
		Map<String, String> token = authService.login(request);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", token.get("accessToken"));
		httpHeaders.add("Authorization_refresh", token.get("refreshToken"));

		return new BaseResponse<>(httpHeaders, BaseResponseStatus.LOGIN_SUCCESS);
	}

	@PostMapping("/users/register")
	public BaseResponse<HttpHeaders> signUp(@RequestBody AuthRequestDto.signUpDto request) {
		Map<String, String> token = authService.signUp(request);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", token.get("accessToken"));
		httpHeaders.add("Authorization_refresh", token.get("refreshToken"));

		return new BaseResponse<>(httpHeaders, BaseResponseStatus.REGISTER_SUCCESS);
	}

	@DeleteMapping("/users/logout")
	public BaseResponse<Void> logout(@RequestHeader("Authorization") String accessToken,
		@AuthenticationPrincipal User user) {
		authService.logout(accessToken, user);
		return new BaseResponse<>(BaseResponseStatus.LOGOUT_SUCCESS);
	}

	@PostMapping("/token/reissue")
	public BaseResponse<HttpHeaders> reissue(@RequestBody RefreshToken refreshToken) {
		Map<String, String> token = authService.reissue(refreshToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", token.get("accessToken"));

		return new BaseResponse<>(httpHeaders, BaseResponseStatus.REISSUE_TOKEN_SUCCESS);
	}

	@GetMapping("/users")
	public BaseResponse<Map<String, Boolean>> isOverlapped(@RequestParam String nickname) {
		Map<String, Boolean> isOverlapped = new HashMap<>();

		if (authService.isOverlapped(nickname)) {
			isOverlapped.put("is_overlapped", true);
			return new BaseResponse<>(BaseResponseStatus.NICKNAME_DUPLICATED, isOverlapped);
		}

		isOverlapped.put("is_overlapped", false);
		return new BaseResponse<>(BaseResponseStatus.NICKNAME_NOT_DUPLICATED, isOverlapped);
	}
}
