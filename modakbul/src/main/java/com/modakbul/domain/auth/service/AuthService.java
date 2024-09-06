package com.modakbul.domain.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.auth.dto.CheckNicknameDto;
import com.modakbul.domain.auth.dto.LoginReqDto;
import com.modakbul.domain.auth.dto.SignUpReqDto;
import com.modakbul.domain.auth.entity.LogoutToken;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.repository.LogoutTokenRepository;
import com.modakbul.domain.auth.repository.RefreshTokenRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.UserRole;
import com.modakbul.domain.user.enums.UserStatus;
import com.modakbul.domain.user.repository.CategoryRepository;
import com.modakbul.domain.user.repository.UserCategoryRepository;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.auth.jwt.JwtProvider;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;
import com.modakbul.global.s3.service.S3ImageService;
import com.vane.badwordfiltering.BadWordFiltering;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	private final UserRepository userRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final LogoutTokenRepository logoutTokenRepository;
	private final JwtProvider jwtProvider;
	@Value("${jwt.refresh-token.expiration-time}")
	private Long refreshTokenExpirationTime;
	private final S3ImageService s3ImageService;

	public Map<String, String> login(LoginReqDto request) {
		Map<String, String> token = new HashMap<>();
		User findUser = userRepository.findByEmailAndProvider(request.getEmail(),
			request.getProvider()).orElse(null);

		findUser.updateFcmToken(request.getFcmToken());

		if (findUser == null) {
			throw new BaseException(BaseResponseStatus.USER_NOT_EXIST);
		} else {
			String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());
			String refreshToken = jwtProvider.createRefreshToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());

			RefreshToken addRefreshToken = new RefreshToken(findUser.getId(), refreshToken, refreshTokenExpirationTime);
			refreshTokenRepository.save(addRefreshToken);

			token.put("Authorization", "Bearer " + accessToken);
			token.put("Authorization_refresh", "Bearer " + refreshToken);

			return token;
		}
	}

	public Map<String, String> signUp(MultipartFile image, SignUpReqDto request) {
		Map<String, String> token = new HashMap<>();

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
			.userJob(request.getUserJob())
			.isVisible(true)
			.image(s3ImageService.upload(image))
			.userRole(UserRole.NORMAL)
			.userStatus(UserStatus.ACTIVE)
			.fcmToken(request.getFcmToken())
			.build();

		userRepository.save(addUser);

		request.getCategoryNames().forEach(categoryName ->
			categoryRepository.findByCategoryName(categoryName)
				.ifPresentOrElse(
					category -> userCategoryRepository.save(
						UserCategory.builder()
							.user(addUser)
							.category(category)
							.build()
					),
					() -> {
						throw new BaseException(BaseResponseStatus.CATEGORY_NOT_EXIST);
					}
				)
		);

		RefreshToken addRefreshToken = new RefreshToken(addUser.getId(), refreshToken, refreshTokenExpirationTime);
		refreshTokenRepository.save(addRefreshToken);

		token.put("Authorization", "Bearer " + accessToken);
		token.put("Authorization_refresh", "Bearer " + refreshToken);

		return token;
	}

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

		String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
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
