package com.modakbul.domain.user.dto;

import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.UserJob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockListResDto {
	private String image;
	private String nickname;
	private CategoryName categoryName;
	private UserJob job;
}
