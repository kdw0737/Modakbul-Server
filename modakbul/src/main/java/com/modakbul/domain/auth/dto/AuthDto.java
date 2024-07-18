package com.modakbul.domain.auth.dto;

import com.modakbul.domain.user.enums.Provider;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class AuthDto {
	private Long userId;
	@Enumerated(EnumType.STRING)
	private Provider provider;
	private String email;
	private String nickname;
	private String role;
}
