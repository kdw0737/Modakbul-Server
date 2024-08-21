package com.modakbul.domain.board.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.modakbul.domain.user.enums.CategoryName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDetailsResDto {
	private List<String> cafeImageUrls;
	private String title;
	private String nickname;
	private String userImage;
	private String createdAt;
	private CategoryName categoryName;
	private int recruitCount;
	private int currentCount;
	private LocalDate meetingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String content;
}
