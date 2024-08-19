package com.modakbul.domain.auth.dto;

import com.modakbul.domain.user.enums.Provider;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReqDto {
	private String email;
	@Enumerated(EnumType.STRING)
	private Provider provider;
}
