package com.modakbul.domain.auth.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modakbul.domain.auth.dto.AppleLoginReqDto;
import com.modakbul.domain.auth.dto.AppleSignUpReqDto;
import com.modakbul.domain.auth.entity.AppleRefreshToken;
import com.modakbul.domain.auth.entity.RefreshToken;
import com.modakbul.domain.auth.repository.AppleRefreshTokenRepository;
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
import com.modakbul.global.common.response.BaseResponseStatus;
import com.modakbul.global.s3.service.S3ImageService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleService {
	@Value("${apple.team.id}")
	private String APPLE_TEAM_ID;

	@Value("${apple.login.key}")
	private String APPLE_LOGIN_KEY;

	@Value("${apple.client.id}")
	private String APPLE_CLIENT_ID;

	@Value("${apple.private.key}")
	private String APPLE_PRIVATE_KEY;

	private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

	private final UserRepository userRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;
	@Value("${jwt.refresh-token.expiration-time}")
	private Long refreshTokenExpirationTime;
	private final S3ImageService s3ImageService;
	private final AppleRefreshTokenRepository appleRefreshTokenRepository;

	@Transactional
	public Map<String, String> login(AppleLoginReqDto request) throws
		JsonProcessingException, IOException {
		Map<String, String> token = new HashMap<>();

		JsonNode node = getNode(request.getAuthorizationCode());
		String email = getEmail(node.path("id_token").asText());
		Provider provider = Provider.APPLE;

		User findUser = userRepository.findByEmailAndProvider(email, provider).orElse(null);

		if (findUser == null) {
			throw new BaseException(BaseResponseStatus.USER_NOT_EXIST);
		} else if ((findUser.getUserStatus()).equals(UserStatus.DELETED)) {
			throw new BaseException(BaseResponseStatus.WITHDRAWAL_USER);
		} else {
			findUser.updateFcmToken(request.getFcm());

			AppleRefreshToken findAppleRefreshToken = appleRefreshTokenRepository.findById(findUser.getId())
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REFRESHTOKEN_EXPIRED));
			findAppleRefreshToken.updateRefreshToken(node.path("refresh_token").asText());

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

	public Map<String, String> signUp(MultipartFile image, AppleSignUpReqDto request) throws IOException {
		JsonNode node = getNode(request.getUser().getAuthorizationCode());

		String email = getEmail(node.path("id_token").asText());
		Provider provider = Provider.APPLE;

		Map<String, String> token = new HashMap<>();
		User findUser = userRepository.findByEmailAndProvider(email, provider).orElse(null);

		if (findUser != null) {
			throw new BaseException(BaseResponseStatus.USER_EXIST);
		}

		String accessToken = jwtProvider.createAccessToken(provider, email, request.getUser().getNickname());
		String refreshToken = jwtProvider.createRefreshToken(provider, email, request.getUser().getNickname());

		User addUser = User.builder()
			.email(email)
			.provider(provider)
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

		AppleRefreshToken addAppleRefreshToken = new AppleRefreshToken(addUser.getId(),
			node.path("refresh_token").asText());
		appleRefreshTokenRepository.save(addAppleRefreshToken);

		RefreshToken addRefreshToken = new RefreshToken(addUser.getId(), refreshToken, refreshTokenExpirationTime);
		refreshTokenRepository.save(addRefreshToken);

		token.put("Authorization", "Bearer " + accessToken);
		token.put("Authorization_refresh", "Bearer " + refreshToken);

		return token;
	}

	public void withdrawal(User user) throws IOException {
		revoke(user);

		user.updateUserStatus(UserStatus.DELETED);
		userRepository.save(user);
	}

	public void revoke(User user) throws IOException {
		AppleRefreshToken findAppleRefreshToken = appleRefreshTokenRepository.findById(user.getId())
			.orElseThrow(() -> new BaseException(BaseResponseStatus.REFRESHTOKEN_EXPIRED));

		RestTemplate restTemplate = new RestTemplateBuilder().build();
		String revokeUrl = APPLE_AUTH_URL + "/auth/revoke";

		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", APPLE_CLIENT_ID);
		params.add("client_secret", createClientSecret());
		params.add("token", findAppleRefreshToken.getRefreshToken());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

		restTemplate.postForEntity(revokeUrl, httpEntity, String.class);
	}

	public String getEmail(String idToken) throws JsonProcessingException {
		if (idToken == null) {
			throw new BaseException(BaseResponseStatus.CODE_NOT_EXIST);
		}
		String[] parts = idToken.split("\\.");
		String payload = parts[1];
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String decodedPayload = new String(decoder.decode(payload));

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode payloadJson = objectMapper.readTree(decodedPayload);
		return payloadJson.get("email").asText();
	}

	private String createClientSecret() throws IOException {
		Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
		Map<String, Object> jwtHeader = new HashMap<>();
		jwtHeader.put("kid", APPLE_LOGIN_KEY); //애플 개발자 사이트의 key 탭에서 앱에 등록한 Sign in with Apple의 Key ID
		jwtHeader.put("alg", "ES256"); //알고리즘 (ES256 사용)

		return Jwts.builder()
			.setHeader(jwtHeader)
			.setIssuer(APPLE_TEAM_ID) //애플 개발자 Team ID
			.setIssuedAt(new Date(System.currentTimeMillis())) //발급 시각 (issued at)
			.setExpiration(expirationDate) //만료 시각 (발급으로부터 6개월 미만)
			.setAudience(APPLE_AUTH_URL) //"https://appleid.apple.com/"
			.setSubject(APPLE_CLIENT_ID) //App bundle ID
			.signWith(SignatureAlgorithm.ES256, APPLE_PRIVATE_KEY)
			.compact();
	}

	public JsonNode getNode(String code) throws IOException { //회원 정보 얻을 수 있는 함수
		RestTemplate restTemplate = new RestTemplateBuilder().build();
		String authUrl = APPLE_AUTH_URL + "/auth/token";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", APPLE_CLIENT_ID);
		params.add("client_secret", createClientSecret());
		params.add("grant_type", "authorization_code");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(authUrl, httpEntity, String.class);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(response.getBody());
	}
}
