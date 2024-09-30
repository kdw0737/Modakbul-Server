package com.modakbul.domain.user.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.user.enums.CategoryName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyMatchesRequestHistoryDto {
	private String title;
	private Long boardId;
	private Long matchId;
	private CategoryName categoryName;
	private Integer recruitCount;
	private Integer currentCount;
	private LocalDate meetingDate;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private BoardStatus boardStatus;
	private MatchStatus matchStatus;
	private Long cafeId;
	private String cafeName;
}
