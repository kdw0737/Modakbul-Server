package com.modakbul.domain.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash("logoutAccessToken")
@Builder
public class LogoutToken {
	@Id
	private String accessToken;
	@TimeToLive
	private Long expiration;

	public LogoutToken(String accessToken, Long expiration) {
		this.accessToken = accessToken;
		this.expiration = expiration;
	}
}

