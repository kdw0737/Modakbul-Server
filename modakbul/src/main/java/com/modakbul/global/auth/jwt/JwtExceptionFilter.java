package com.modakbul.global.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modakbul.global.common.response.BaseResponseStatus;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			String message = e.getMessage();
			if (BaseResponseStatus.WRONG_TYPE_TOKEN.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.WRONG_TYPE_TOKEN);
			} else if (BaseResponseStatus.DAMAGED_TOKEN.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.DAMAGED_TOKEN);
			} else if (BaseResponseStatus.ACCESSTOKEN_EXPIRED.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.ACCESSTOKEN_EXPIRED);
			} else if (BaseResponseStatus.UNSUPPORTED_TOKEN.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.UNSUPPORTED_TOKEN);
			} else if (BaseResponseStatus.BLACKLIST_TOKEN.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.BLACKLIST_TOKEN);
			} else if (BaseResponseStatus.USER_NOT_EXIST.getMessage().equals(message)) {
				setResponse(response, BaseResponseStatus.USER_NOT_EXIST);
			}
		}
	}

	private void setResponse(HttpServletResponse response, BaseResponseStatus baseResponseStatus) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("status", baseResponseStatus.isStatus());
		jsonResponse.put("code", baseResponseStatus.getCode());
		jsonResponse.put("message", baseResponseStatus.getMessage());
		objectMapper.writeValue(response.getWriter(), jsonResponse);
	}
}
