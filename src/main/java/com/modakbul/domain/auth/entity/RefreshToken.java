package com.modakbul.domain.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RedisHash(value = "userId")
public class RefreshToken {
	@Id
	private Long id;
	@Indexed
	private String refreshToken;
	@TimeToLive
	private Long expirationTime;

	public RefreshToken(Long id, String refreshToken, Long expirationTime) {
		this.id = id;
		this.refreshToken = refreshToken;
		this.expirationTime = expirationTime;
	}
}