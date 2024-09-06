package com.modakbul.domain.board.dto;

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
public class CafeDto {
	private String cafeName;
	private String streetAddress;
	private String image;
	private List<OpeningHour> openingHour;
	private Outlet outlet;
	private GroupSeat groupSeat;
	private Congestion congestion;
}
