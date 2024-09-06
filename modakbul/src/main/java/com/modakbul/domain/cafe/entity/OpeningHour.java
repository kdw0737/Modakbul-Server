<<<<<<< HEAD
package com.modakbul.domain.cafe.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.modakbul.domain.cafe.enums.OpeningStatus;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
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
public class OpeningHour extends BaseEntity {
	@Enumerated
	private DayOfWeek dayOfWeek; //요일

	private OpeningStatus openingStatus; // 오픈, 마감

	private LocalTime openedAt; // 오픈 시간

	private LocalTime closedAt; // 마감 시간
}
=======
package com.modakbul.domain.cafe.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.modakbul.domain.cafe.enums.OpeningStatus;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
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
public class OpeningHour extends BaseEntity {
	@Enumerated
	private DayOfWeek dayOfWeek; //요일

	private OpeningStatus openingStatus; // 오픈, 마감

	private LocalTime openedAt; // 오픈 시간

	private LocalTime closedAt; // 마감 시간
}
>>>>>>> e2b2879279d196a48e04d5a4824fdce288a5471a
