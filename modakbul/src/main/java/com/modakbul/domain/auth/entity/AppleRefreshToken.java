package com.modakbul.domain.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RedisHash(value = "userId")
public class AppleRefreshToken {
	@Id
	private Long id;
	@Indexed
	private String refreshToken;

	public AppleRefreshToken(Long id, String refreshToken) {
		this.id = id;
		this.refreshToken = refreshToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
