package com.modakbul.domain.auth.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.auth.dto.AppleLoginReqDto;
import com.modakbul.domain.auth.dto.AppleSignUpReqDto;
import com.modakbul.domain.auth.service.AppleService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppleController {

	private final AppleService appleService;

	@PostMapping("/users/login/apple")
	public ResponseEntity<BaseResponse<Void>> login(@RequestBody AppleLoginReqDto request) throws IOException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(appleService.login(request));

		return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS), httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/users/register/apple")
	public ResponseEntity<BaseResponse<Void>> signUp(
		@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "user") AppleSignUpReqDto request) throws IOException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(appleService.signUp(image, request));

		return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.REGISTER_SUCCESS), httpHeaders,
			HttpStatus.OK);
	}

	@DeleteMapping("/users/withdrawal/apple")
	public BaseResponse<Void> withdrawal(@AuthenticationPrincipal User user) throws IOException {
		appleService.withdrawal(user);
		return new BaseResponse<>(BaseResponseStatus.WITHDRAWAL_SUCCESS);
	}
}
