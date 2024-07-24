package com.modakbul.domain.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.auth.dto.AuthRequestDto;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.service.AuthService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/users/login")
	public BaseResponse<Void> login(@RequestBody AuthRequestDto.loginDto request) {
		return authService.login(request);
	}

	@PostMapping("/users/register")
	public BaseResponse<Void> signUp(@RequestBody AuthRequestDto.signUpDto request) {
		return authService.signUp(request);
	}

	@DeleteMapping("/users/logout")
	public BaseResponse<Void> logout(@RequestHeader("Authorization") String accessToken,
		@AuthenticationPrincipal User user) {
		return authService.logout(accessToken, user);
	}

	@PostMapping("/token/reissue")
	public BaseResponse<Void> reissue(@RequestBody RefreshToken refreshToken) {
		return authService.reissue(refreshToken);
	}
}
