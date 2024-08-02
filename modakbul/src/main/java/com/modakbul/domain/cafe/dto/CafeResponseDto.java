package com.modakbul.domain.cafe.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.modakbul.domain.cafe.entity.Address;
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

public class CafeResponseDto {

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
	public static class CafeListDto {
		private String cafeImage;
		private String cafeName;
		private int meetingCount;
		private Address streetAddress;
		private OpeningHour openingHour;
		private Outlet outlet;
		private Congestion congestion;
		private GroupSeat groupSeat;
	}
}
