package com.modakbul.domain.board.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

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
public class UpdateBoardResDto {
	private String cafeName;
	private String streetAddress;
	private CategoryName categoryName;
	private int recruitCount;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate meetingDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	private String title;
	private String content;
}
