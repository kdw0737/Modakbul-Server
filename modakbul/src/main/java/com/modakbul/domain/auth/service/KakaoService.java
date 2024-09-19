package com.modakbul.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.auth.dto.AuthResDto;
import com.modakbul.domain.auth.dto.KakaoLoginReqDto;
import com.modakbul.domain.auth.dto.KakaoSignUpReqDto;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.repository.RefreshTokenRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.Provider;
import com.modakbul.domain.user.enums.UserRole;
import com.modakbul.domain.user.enums.UserStatus;
import com.modakbul.domain.user.repository.CategoryRepository;
import com.modakbul.domain.user.repository.UserCategoryRepository;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.auth.jwt.JwtProvider;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;
import com.modakbul.global.s3.service.S3ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

	private final UserRepository userRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;
	@Value("${jwt.refresh-token.expiration-time}")
	private Long refreshTokenExpirationTime;
	private final S3ImageService s3ImageService;

	public ResponseEntity<BaseResponse<AuthResDto>> login(KakaoLoginReqDto request) {
		HttpHeaders httpHeaders = new HttpHeaders();
		User findUser = userRepository.findByEmailAndProvider(request.getEmail(),
			Provider.KAKAO).orElse(null);

		if (findUser == null) {
			AuthResDto authResDto = AuthResDto.builder().userId(-1L).build();
			return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.USER_NOT_EXIST, authResDto),
				httpHeaders, HttpStatus.OK);
		} else if ((findUser.getUserStatus()).equals(UserStatus.DELETED)) {
			throw new BaseException(BaseResponseStatus.WITHDRAWAL_USER);
		} else {
			findUser.updateFcmToken(request.getFcm());

			String accessToken = jwtProvider.createAccessToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());
			String refreshToken = jwtProvider.createRefreshToken(findUser.getProvider(), findUser.getEmail(),
				findUser.getNickname());

			RefreshToken addRefreshToken = new RefreshToken(findUser.getId(), refreshToken, refreshTokenExpirationTime);
			refreshTokenRepository.save(addRefreshToken);

			httpHeaders.set("Authorization", "Bearer " + accessToken);
			httpHeaders.set("Authorization_refresh", "Bearer " + refreshToken);

			AuthResDto authResDto = AuthResDto.builder().userId(findUser.getId()).build();
			return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, authResDto),
				httpHeaders, HttpStatus.OK);
		}
	}

	public ResponseEntity<BaseResponse<AuthResDto>> signUp(MultipartFile image, KakaoSignUpReqDto request) {
		User findUser = userRepository.findByEmailAndProvider(request.getUser().getEmail(),
			Provider.KAKAO).orElse(null);

		if (findUser != null) {
			throw new BaseException(BaseResponseStatus.USER_EXIST);
		}

		String accessToken = jwtProvider.createAccessToken(Provider.KAKAO, request.getUser().getEmail(),
			request.getUser().getNickname());
		String refreshToken = jwtProvider.createRefreshToken(Provider.KAKAO, request.getUser().getEmail(),
			request.getUser().getNickname());

		User addUser = User.builder()
			.email(request.getUser().getEmail())
			.provider(Provider.KAKAO)
			.birth(request.getUser().getBirth())
			.name(request.getUser().getName())
			.nickname(request.getUser().getNickname())
			.gender(request.getUser().getGender())
			.userJob(request.getUser().getJob())
			.isVisible(true)
			.image(s3ImageService.upload(image))
			.userRole(UserRole.NORMAL)
			.userStatus(UserStatus.ACTIVE)
			.fcmToken(request.getUser().getFcm())
			.build();
		userRepository.save(addUser);

		request.getUser().getCategories().forEach(categoryName ->
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

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		httpHeaders.set("Authorization_refresh", "Bearer " + refreshToken);

		AuthResDto authResDto = AuthResDto.builder().userId(addUser.getId()).build();
		return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, authResDto),
			httpHeaders, HttpStatus.OK);
	}

	public void withdrawal(User user) {
		user.updateUserStatus(UserStatus.DELETED);
		userRepository.save(user);
	}
}
