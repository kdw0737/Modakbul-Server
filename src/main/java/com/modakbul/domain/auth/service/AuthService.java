package com.modakbul.domain.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modakbul.domain.auth.dto.CheckNicknameDto;
import com.modakbul.domain.auth.entity.LogoutToken;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.repository.LogoutTokenRepository;
import com.modakbul.domain.auth.repository.RefreshTokenRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.auth.jwt.JwtProvider;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;
import com.vane.badwordfiltering.BadWordFiltering;

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

	public void logout(String accessToken, User user) {
		Long expiration = jwtProvider.getExpiration(accessToken);

		refreshTokenRepository.deleteById(user.getId());
		logoutTokenRepository.save(new LogoutToken(accessToken, expiration / 1000));
	}

	public Map<String, String> reissue(String refreshToken) {
		Map<String, String> token = new HashMap<>();
		RefreshToken findToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.REFRESHTOKEN_EXPIRED));

		User findUser = userRepository.findById(findToken.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getProvideId(),
			findUser.getNickname());

		token.put("Authorization", "Bearer " + accessToken);

		return token;
	}

	public CheckNicknameDto checkNickname(String nickname) {
		if (isAbuse(nickname)) {
			return CheckNicknameDto.builder()
				.isOverlapped(false)
				.isAbuse(true)
				.build();
		} else if (isOverlapped(nickname)) {
			return CheckNicknameDto.builder()
				.isOverlapped(true)
				.isAbuse(false)
				.build();
		} else {
			return CheckNicknameDto.builder()
				.isOverlapped(false)
				.isAbuse(false)
				.build();
		}
	}

	public boolean isOverlapped(String nickname) {
		Optional<User> findNickname = userRepository.findByNickname(nickname);

		if (findNickname.isPresent()) {
			return true;
		}
		return false;
	}

	public boolean isAbuse(String nickname) {
		BadWordFiltering badWordFiltering = new BadWordFiltering();

		if (badWordFiltering.blankCheck(nickname)) {
			return true;
		}
		return false;
	}
}
