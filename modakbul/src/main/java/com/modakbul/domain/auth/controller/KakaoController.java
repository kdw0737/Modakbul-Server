package com.modakbul.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.auth.dto.AuthResDto;
import com.modakbul.domain.auth.dto.KakaoLoginReqDto;
import com.modakbul.domain.auth.dto.KakaoSignUpReqDto;
import com.modakbul.domain.auth.service.KakaoService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KakaoController {

	private final KakaoService kakaoService;

	@PostMapping("/users/login/kakao")
	public ResponseEntity<BaseResponse<AuthResDto>> login(@RequestBody KakaoLoginReqDto request) {
		return kakaoService.login(request);
	}

	@PostMapping(value = "/users/register/kakao")
	public ResponseEntity<BaseResponse<AuthResDto>> signUp(
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "user") KakaoSignUpReqDto request) {
		return kakaoService.signUp(image, request);
	}

	@DeleteMapping("/users/withdrawal/kakao")
	public BaseResponse<Void> withdrawal(@AuthenticationPrincipal User user,
		@RequestHeader("Authorization") String accessToken) {
		kakaoService.withdrawal(user, accessToken);
		return new BaseResponse<>(BaseResponseStatus.WITHDRAWAL_SUCCESS);
	}
}
