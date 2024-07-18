package com.modakbul.global.auth.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.modakbul.domain.auth.dto.AuthDto;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// request Header에서 AccessToken을 가져온다.
		String findAccessToken = jwtProvider.resolveToken(request);

		// 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
		if (!StringUtils.hasText(findAccessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		// AccessToken을 검증하고, 만료되었을경우 예외를 발생시킨다.
		if (jwtProvider.isExpired(findAccessToken)) {
			throw new JwtException("Access Token 만료");
		}

		// AccessToken의 값이 있고, 유효한 경우에 진행한다.
		if (!jwtProvider.isExpired(findAccessToken) && !jwtProvider.isTokenBlacklist(findAccessToken)) {
			// AccessToken 내부의 payload에 있는 email과 provider로 user를 조회한다. 없다면 예외를 발생시킨다 -> 정상 케이스가 아님
			User user = userRepository.findByEmailAndProvider(jwtProvider.getEmail(findAccessToken),
					jwtProvider.getProvider(findAccessToken))
				.orElseThrow(IllegalStateException::new);

			// SecurityContext에 등록할 User 객체를 만들어준다.
			AuthDto authDto = AuthDto.builder()
				.userId(user.getId())
				.provider(user.getProvider())
				.email(user.getEmail())
				.role("ROLE_".concat(String.valueOf(user.getUserRole())))
				.nickname(user.getNickname())
				.build();

			// SecurityContext에 인증 객체를 등록해준다.
			Authentication auth = getAuthentication(authDto);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(AuthDto user) {
		return new UsernamePasswordAuthenticationToken(user, "", List.of(new SimpleGrantedAuthority(user.getRole())));
	}

}
