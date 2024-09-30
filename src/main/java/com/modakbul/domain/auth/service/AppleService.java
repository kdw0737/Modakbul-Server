package com.modakbul.domain.auth.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.modakbul.domain.auth.dto.ApplePublicKeyDto;
import com.modakbul.domain.auth.dto.AppleSignUpReqDto;
import com.modakbul.domain.auth.dto.AuthResDto;
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
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;
import com.modakbul.global.s3.service.S3ImageService;

import io.jsonwebtoken.Claims;
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
	public ResponseEntity<BaseResponse<AuthResDto>> login(AppleLoginReqDto request) throws
		IOException,
		InvalidKeySpecException,
		NoSuchAlgorithmException {
		HttpHeaders httpHeaders = new HttpHeaders();
		JsonNode node = getNode(request.getAuthorizationCode());
		String email = (String)getClaims(node.path("id_token").asText()).get("email");
		Provider provider = Provider.APPLE;

		User findUser = userRepository.findByEmailAndProvider(email, provider).orElse(null);

		if (findUser == null) {
			AuthResDto authResDto = AuthResDto.builder().userId(-1L).build();
			return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.USER_NOT_EXIST, authResDto),
				httpHeaders, HttpStatus.OK);
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

			httpHeaders.set("Authorization", "Bearer " + accessToken);
			httpHeaders.set("Authorization_refresh", "Bearer " + refreshToken);

			AuthResDto authResDto = AuthResDto.builder().userId(findUser.getId()).build();
			return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, authResDto),
				httpHeaders, HttpStatus.OK);
		}
	}

	public ResponseEntity<BaseResponse<AuthResDto>> signUp(MultipartFile image, AppleSignUpReqDto request) throws
		IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		JsonNode node = getNode(request.getAuthorizationCode());

		String email = (String)getClaims(node.path("id_token").asText()).get("email");
		Provider provider = Provider.APPLE;

		User findUser = userRepository.findByEmailAndProvider(email, provider).orElse(null);

		if (findUser != null) {
			throw new BaseException(BaseResponseStatus.USER_EXIST);
		}

		String accessToken = jwtProvider.createAccessToken(provider, email, request.getNickname());
		String refreshToken = jwtProvider.createRefreshToken(provider, email, request.getNickname());

		User addUser = User.builder()
			.email(email)
			.provider(provider)
			.birth(request.getBirth())
			.name(request.getName())
			.nickname(request.getNickname())
			.gender(request.getGender())
			.userJob(request.getJob())
			.isVisible(true)
			.image(s3ImageService.upload(image))
			.userRole(UserRole.NORMAL)
			.userStatus(UserStatus.ACTIVE)
			.fcmToken(request.getFcm())
			.build();
		userRepository.save(addUser);

		request.getCategories().forEach(categoryName ->
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

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "Bearer " + accessToken);
		httpHeaders.set("Authorization_refresh", "Bearer " + refreshToken);

		AuthResDto authResDto = AuthResDto.builder().userId(addUser.getId()).build();
		return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.LOGIN_SUCCESS, authResDto),
			httpHeaders, HttpStatus.OK);
	}

	public void withdrawal(User user) {
		revoke(user);

		user.updateUserStatus(UserStatus.DELETED);
		userRepository.save(user);
	}

	public void revoke(User user) {
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

	public Claims getClaims(String idToken) throws
		JsonProcessingException,
		UnsupportedEncodingException,
		InvalidKeySpecException,
		NoSuchAlgorithmException {
		List<ApplePublicKeyDto> applePublicKeys = getPublicKey();

		String headerOfIdToken = idToken.substring(0, idToken.indexOf("."));
		Map<String, String> header = new ObjectMapper().readValue(
			new String(Base64.getDecoder().decode(headerOfIdToken), "UTF-8"), Map.class);
		ApplePublicKeyDto applePublicKey = applePublicKeys.stream()
			.filter(key -> key.getKid().equals(header.get("kid")) && key.getAlg().equals(header.get("alg")))
			.findFirst()
			.orElseThrow(() -> new BaseException(BaseResponseStatus.SEARCH_APPLE_PUBLIC_KEY_FAILED));

		byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
		byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

		BigInteger n = new BigInteger(1, nBytes);
		BigInteger e = new BigInteger(1, eBytes);

		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
		KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
	}

	public List<ApplePublicKeyDto> getPublicKey() throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplateBuilder().build();
		String authUrl = APPLE_AUTH_URL + "/auth/keys";

		ResponseEntity<String> response = restTemplate.getForEntity(authUrl, String.class);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode publicKeys = mapper.readTree(response.getBody()).get("keys");

		List<ApplePublicKeyDto> applePublicKeys = new ArrayList<>();
		for (JsonNode publicKey : publicKeys) {
			ApplePublicKeyDto applePublicKey = ApplePublicKeyDto.builder()
				.kty(publicKey.get("kty").asText())
				.kid(publicKey.get("kid").asText())
				.use(publicKey.get("use").asText())
				.alg(publicKey.get("alg").asText())
				.n(publicKey.get("n").asText())
				.e(publicKey.get("e").asText())
				.build();
			applePublicKeys.add(applePublicKey);
		}

		return applePublicKeys;
	}

	private String createClientSecret() {
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
