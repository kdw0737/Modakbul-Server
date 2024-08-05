package com.modakbul.domain.board.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.modakbul.domain.cafe.entity.OpeningHour;
import com.modakbul.domain.cafe.enums.Congestion;
import com.modakbul.domain.cafe.enums.GroupSeat;
import com.modakbul.domain.cafe.enums.Outlet;
import com.modakbul.domain.user.enums.CategoryName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardResponseDto {
	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateBoardDto {
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

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class MeetingDto {
		private BoardResponseDto.CafeDto cafe;
		private List<BoardDto> boards;
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class BoardDto {
		private String title;
		private CategoryName categoryName;
		private int recruitCount;
		private int currentCount;
		private LocalDate meetingDate;
		private LocalTime startTime;
		private LocalTime endTime;
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CafeDto {
		private String cafeName;
		private String streetAddress;
		private String image;
		private List<OpeningHour> openingHour;
		private Outlet outlet;
		private GroupSeat groupSeat;
		private Congestion congestion;
	}

	@Builder
	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class BoardDetails {
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
}
