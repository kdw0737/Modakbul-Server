package com.modakbul.domain.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.auth.dto.AuthRequestDto;
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
	public ResponseEntity<BaseResponse> login(@RequestBody AuthRequestDto.LoginDto request) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(authService.login(request));

		return new ResponseEntity(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS), httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/users/register")
	public ResponseEntity<BaseResponse> signUp(@RequestPart(value = "image", required = false) MultipartFile image,
		@RequestPart(value = "user") AuthRequestDto.SignUpDto request) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(authService.signUp(image, request));

		return new ResponseEntity(new BaseResponse<>(BaseResponseStatus.REGISTER_SUCCESS), httpHeaders, HttpStatus.OK);
	}

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
