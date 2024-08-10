package com.modakbul.domain.user.dto;

import java.util.List;

import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.UserJob;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResDto {
	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ProfileDto {
		private String nickname;
		private String image;
		private Boolean isVisible;
		private UserJob userJob;
		private List<CategoryName> categories;
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UserCafeDto {
		private long cafeId;
		private String image;
		private String name;
		private String streetAddress;

	}
}
