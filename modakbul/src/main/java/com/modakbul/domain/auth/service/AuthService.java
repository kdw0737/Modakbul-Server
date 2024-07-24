package com.modakbul.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

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
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

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
	@Value("${jwt.refresh-token.expiration-time}")
	private Long refreshTokenExpirationTime;

	public BaseResponse<Void> login(AuthRequestDto.loginDto request) {
		User findUser = userRepository.findByEmailAndProvider(request.getEmail(),
			request.getProvider()).orElse(null);

		if (findUser == null) {
			throw new BaseException(BaseResponseStatus.USER_NOT_EXIST);
		} else {
			HttpHeaders httpHeaders = new HttpHeaders();

			String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());
			String refreshToken = jwtProvider.createRefreshToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());

			RefreshToken addRefreshToken = new RefreshToken(findUser.getId(), refreshToken, refreshTokenExpirationTime);
			refreshTokenRepository.save(addRefreshToken);

			httpHeaders.add("Authorization", accessToken);
			httpHeaders.add("Authorization_refresh", refreshToken);

			return new BaseResponse<>(httpHeaders, BaseResponseStatus.LOGIN_SUCCESS);
		}
	}

	public BaseResponse<Void> signUp(AuthRequestDto.signUpDto request) {
		String accessToken = jwtProvider.createAccessToken(request.getProvider(), request.getEmail(),
			request.getNickname());
		String refreshToken = jwtProvider.createRefreshToken(request.getProvider(), request.getEmail(),
			request.getNickname());

		User addUser = User.builder()
			.email(request.getEmail())
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

		RefreshToken addRefreshToken = new RefreshToken(addUser.getId(), refreshToken, refreshTokenExpirationTime);
		refreshTokenRepository.save(addRefreshToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", accessToken);
		httpHeaders.add("Authorization_refresh", refreshToken);

		return new BaseResponse<>(httpHeaders, BaseResponseStatus.REGISTER_SUCCESS);
	}

	public BaseResponse<Void> logout(String accessToken, User user) {
		Long expiration = jwtProvider.getExpiration(accessToken);

		refreshTokenRepository.deleteById(user.getId());
		logoutTokenRepository.save(new LogoutToken(accessToken, expiration / 1000));

		return new BaseResponse<>(BaseResponseStatus.LOGOUT_SUCCESS);
	}

	public BaseResponse<Void> reissue(RefreshToken refreshToken) {
		RefreshToken findToken = refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.REFRESHTOKEN_EXPIRED));

		User findUser = userRepository.findById(findToken.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		HttpHeaders httpHeaders = new HttpHeaders();
		String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
			findUser.getNickname());

		httpHeaders.add("Authorization", accessToken);
		return new BaseResponse<>(httpHeaders, BaseResponseStatus.REISSUE_TOKEN_SUCCESS);
	}
}
