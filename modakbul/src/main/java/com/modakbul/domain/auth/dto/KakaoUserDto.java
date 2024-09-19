package com.modakbul.domain.auth.dto;

import java.util.List;

import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.Gender;
import com.modakbul.domain.user.enums.UserJob;

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
public class KakaoUserDto {
	private String email;
	private String name;
	private String nickname;
	private String birth;
	private Gender gender;
	@Enumerated(EnumType.STRING)
	private List<CategoryName> categories;
	private UserJob job;
	private String fcm;
}
