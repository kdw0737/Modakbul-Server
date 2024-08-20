package com.modakbul.domain.information.dto;

import com.modakbul.domain.cafe.enums.GroupSeat;
import com.modakbul.domain.cafe.enums.Outlet;
import com.modakbul.domain.information.entity.Address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InformationReqDto {
	private String name;
	private Address address;
	private Outlet outlet;
	private GroupSeat groupSeat;
}

