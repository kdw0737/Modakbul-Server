package com.modakbul.global.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.modakbul.global.auth.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrfConfig) -> csrfConfig.disable())
			.httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증을 비활성화
			.cors(cors -> cors.disable())
			.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				// 세션관리 정책을 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다)

			)
			.formLogin(formLogin -> formLogin.disable())
			.logout(logout -> logout.disable())
			.headers(headers -> headers
				.frameOptions(frameOptions -> frameOptions.disable())
			)
			.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
				.requestMatchers("/users/register/**", "/users/login/**", "/token/**", "/", "/css/**", "/images/**",
					"/js/**", "/favicon.ico").permitAll()
				.anyRequest().authenticated()
			)
			// JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

}
