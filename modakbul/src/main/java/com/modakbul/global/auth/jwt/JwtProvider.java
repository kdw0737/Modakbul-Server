package com.modakbul.global.auth.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.modakbul.domain.auth.entity.LogoutToken;
import com.modakbul.domain.auth.repository.LogoutTokenRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.enums.Provider;
import com.modakbul.global.common.response.BaseResponseStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret}")
	private String secretKey;
	private final LogoutTokenRepository logoutTokenRepository;
	@Value("${jwt.access-token.expiration-time}")
	private Long accessTokenExpirationTime;
	@Value("${jwt.refresh-token.expiration-time}")
	private Long refreshTokenExpirationTime;

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

	public String getNickName(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String createAccessToken(Provider provider, String email, String nickname) {
		return createJwt(provider, email, nickname, Duration.ofSeconds(accessTokenExpirationTime).toMillis());
	}

	public String createRefreshToken(Provider provider, String email, String nickname) {
		return createJwt(provider, email, nickname, Duration.ofSeconds(refreshTokenExpirationTime).toMillis());
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
		return findLogoutToken.isPresent();
	}

	public String resolveToken(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getHeader("Authorization");
	}

	public Long getExpiration(String accessToken) {
		Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();

		Long now = new Date().getTime();
		return (expiration.getTime() - now);
	}

	public Authentication getAuthentication(User user) {
		return new UsernamePasswordAuthenticationToken(user, "",
			List.of(new SimpleGrantedAuthority(String.valueOf(user.getUserRole()))));
	}

	public boolean validateToken(String token) {
		try {
			if (isTokenBlacklist(token)) {
				throw new IllegalArgumentException();
			}
			return !isExpired(token);
		} catch (SignatureException e) {
			throw new JwtException(BaseResponseStatus.WRONG_TYPE_TOKEN.getMessage());
		} catch (MalformedJwtException e) {
			throw new JwtException(BaseResponseStatus.DAMAGED_TOKEN.getMessage());
		} catch (ExpiredJwtException e) {
			throw new JwtException(BaseResponseStatus.ACCESSTOKEN_EXPIRED.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new JwtException(BaseResponseStatus.UNSUPPORTED_TOKEN.getMessage());
		} catch (IllegalArgumentException e) {
			throw new JwtException(BaseResponseStatus.BLACKLIST_TOKEN.getMessage());
		} catch (NullPointerException e) {
			throw new JwtException(BaseResponseStatus.USER_NOT_EXIST.getMessage());
		}
	}
}
