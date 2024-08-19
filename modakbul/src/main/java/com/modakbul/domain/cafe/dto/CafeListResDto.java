package com.modakbul.domain.cafe.dto;

import java.util.List;

import com.modakbul.domain.cafe.entity.OpeningHour;
import com.modakbul.domain.cafe.enums.Congestion;
import com.modakbul.domain.cafe.enums.GroupSeat;
import com.modakbul.domain.cafe.enums.Outlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeListResDto {
	private long cafeId;
	private String cafeImage;
	private String cafeName;
	private int meetingCount;
	private String streetAddress;
	private List<OpeningHour> openingHour;
	private Outlet outlet;
	private Congestion congestion;
	private GroupSeat groupSeat;
}
