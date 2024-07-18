package com.modakbul.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.auth.dto.AuthDto;
import com.modakbul.domain.auth.dto.AuthRequestDto;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/users/login")
	public ResponseEntity<?> login(@RequestBody AuthRequestDto.loginDto request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/users/register")
	public ResponseEntity<?> signUp(@RequestBody AuthRequestDto.signUpDto request) {
		return ResponseEntity.ok(authService.signUp(request));
	}

	@DeleteMapping("/users/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken,
		@AuthenticationPrincipal AuthDto authDto) {
		return ResponseEntity.ok(authService.logout(accessToken, authDto));
	}

	@PostMapping("/token/reissue")
	public ResponseEntity<?> reissue(@RequestBody RefreshToken refreshToken) {
		return ResponseEntity.ok(authService.reissue(refreshToken));
	}

	@GetMapping("/test")
	public ResponseEntity<?> test(@AuthenticationPrincipal AuthDto authDto) {
		return null;
	}
}
