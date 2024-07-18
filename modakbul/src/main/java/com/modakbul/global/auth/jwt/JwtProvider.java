package com.modakbul.global.auth.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.modakbul.domain.auth.entity.LogoutToken;
import com.modakbul.domain.auth.repository.LogoutTokenRepository;
import com.modakbul.domain.user.enums.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("jwt.secret")
	private String secretKey;
	private final LogoutTokenRepository logoutTokenRepository;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String getNickname(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public Provider getProvider(String token) {
		String providerStr = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.get("provider", String.class);
		return Provider.valueOf(providerStr);
	}

	public String getEmail(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email", String.class);
	}

	public String createAccessToken(Provider provider, String email, String nickname) {
		return createJwt(provider, email, nickname, Duration.ofSeconds(30).toMillis());
	}

	public String createRefreshToken(Provider provider, String email, String nickname) {
		return createJwt(provider, email, nickname, Duration.ofSeconds(60).toMillis());
	}

	public String createJwt(Provider provider, String email, String nickname, Long tokenValidTime) {
		Claims claims = Jwts.claims().setSubject(nickname);
		claims.put("provider", provider);
		claims.put("email", email);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public boolean isExpired(String token) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		return claims.getBody().getExpiration().before(new Date()); // Token 만료 날짜가 지금보다 이전이면 만료
	}

	public boolean isTokenBlacklist(String token) {
		Optional<LogoutToken> findLogoutToken = logoutTokenRepository.findById(token);
		if (findLogoutToken.isPresent()) {
			throw new RuntimeException("블랙리스트에 있는 토큰 입니다.");
		}
		return false;
	}

	public String resolveToken(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getHeader("Authorization");
	}

	public Long getExpiration(String accessToken) {
		Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();

		Long now = new Date().getTime();
		return (expiration.getTime() - now);
	}
}
