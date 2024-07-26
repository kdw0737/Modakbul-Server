package com.modakbul.domain.cafe.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Embeddable
public class Address {
	private String streetAddress; // 도로명 주소

	private Double latitude; //위도

	private Double longitude; //경도
}
