package com.modakbul.domain.auth.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.modakbul.domain.auth.dto.AuthDto;
import com.modakbul.domain.auth.dto.AuthRequestDto;
import com.modakbul.domain.auth.entity.LogoutToken;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.repository.LogoutTokenRepository;
import com.modakbul.domain.auth.repository.RefreshTokenRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.enums.UserRole;
import com.modakbul.domain.user.enums.UserStatus;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.auth.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final LogoutTokenRepository logoutTokenRepository;
	private final JwtProvider jwtProvider;

	public ResponseEntity<?> login(AuthRequestDto.loginDto request) {
		User findUser = userRepository.findByEmailAndProvider(request.getEmail(), request.getProvider()).orElse(null);

		if (findUser == null) {
			return new ResponseEntity<>("회원가입이 되어있지 않습니다.", HttpStatus.OK);
		} else {
			HttpHeaders httpHeaders = new HttpHeaders();

			String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());
			String refreshToken = jwtProvider.createRefreshToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());

			RefreshToken addRefreshToken = new RefreshToken(findUser.getId(), refreshToken);
			refreshTokenRepository.save(addRefreshToken);

			httpHeaders.add("Authorization", accessToken);
			httpHeaders.add("Authorization_refresh", refreshToken);

			return new ResponseEntity<>(findUser, httpHeaders, HttpStatus.OK);
		}
	}

	public ResponseEntity<?> signUp(AuthRequestDto.signUpDto request) {
		String accessToken = jwtProvider.createAccessToken(request.getProvider(), request.getEmail(),
			request.getNickname());
		String refreshToken = jwtProvider.createRefreshToken(request.getProvider(), request.getEmail(),
			request.getNickname());

		User addUser = User.builder()
			.email(request.getEmail())
			//.password("123")
			//.provideId("2")
			.provider(request.getProvider())
			.birth(request.getBirth())
			.name(request.getName())
			.nickname(request.getNickname())
			.gender(request.getGender())
			.isVisible(true)
			.image(request.getImage())
			.userRole(UserRole.NORMAL)
			.userStatus(UserStatus.ACTIVE)
			.build();
		userRepository.save(addUser);

		RefreshToken addRefreshToken = new RefreshToken(addUser.getId(), refreshToken);
		refreshTokenRepository.save(addRefreshToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", accessToken);
		httpHeaders.add("Authorization_refresh", refreshToken);

		return new ResponseEntity<>(addUser.getId(), httpHeaders, HttpStatus.CREATED);
	}

	public ResponseEntity<?> logout(String accessToken, AuthDto authDto) {
		Long expiration = jwtProvider.getExpiration(accessToken);

		refreshTokenRepository.deleteById(authDto.getUserId());
		logoutTokenRepository.save(new LogoutToken(accessToken, expiration / 1000));

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> reissue(RefreshToken refreshToken) {
		RefreshToken findToken = refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
			.orElseThrow(() -> new RuntimeException("유효하지 않은 RefreshToken입니다."));

		User findUser = userRepository.findById(findToken.getId())
			.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

		HttpHeaders httpHeaders = new HttpHeaders();
		String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
			findUser.getNickname());

		httpHeaders.add("Authorization", accessToken);
		return new ResponseEntity<>(findUser, httpHeaders, HttpStatus.OK);
	}
}
