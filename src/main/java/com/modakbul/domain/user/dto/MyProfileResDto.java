package com.modakbul.domain.user.dto;

import java.util.List;

import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.UserJob;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyProfileResDto {
	private long id;
	private String nickname;
	private String image;
	private Boolean isGenderVisible;
	private UserJob job;
	private List<CategoryName> categories;
}
