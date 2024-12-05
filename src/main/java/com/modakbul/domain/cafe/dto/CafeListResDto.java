package com.modakbul.domain.cafe.dto;

import java.util.List;

import com.modakbul.domain.cafe.entity.Address;
import com.modakbul.domain.cafe.entity.OpeningHour;
import com.modakbul.domain.cafe.enums.GroupSeat;
import com.modakbul.domain.cafe.enums.Outlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeListResDto {
	private long id;
	private List<String> image;
	private String name;
	private long meetingCount;
	private Address location;
	private List<OpeningHour> openingHour;
	private Outlet outlet;
	private GroupSeat groupSeat;
}
