package com.modakbul.global.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String findAccessToken = jwtProvider.resolveToken(request);

		if (!StringUtils.hasText(findAccessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		if (findAccessToken != null && jwtProvider.validateToken(findAccessToken)) {
			User findUser = userRepository.findByEmailAndProvider(jwtProvider.getEmail(findAccessToken),
				jwtProvider.getProvider(findAccessToken)).orElseThrow(NullPointerException::new);

			Authentication auth = jwtProvider.getAuthentication(findUser);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(request, response);
	}
}
