package com.modakbul.domain.cafe.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class BoardResDto {
	private String title;
	private CategoryName categoryName;
	private int recruitCount;
	private int currentCount;
	private LocalDate meetingDate;
	private LocalTime startTime;
	private LocalTime endTime;
}
