package com.modakbul.domain.information.entity;

import com.modakbul.domain.cafe.entity.Address;
import com.modakbul.domain.cafe.enums.Congestion;
import com.modakbul.domain.cafe.enums.GroupSeat;
import com.modakbul.domain.cafe.enums.Outlet;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Information extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "information_id")
	private Long id;

	private String name;

	private Address address;

	@Enumerated(EnumType.STRING)
	private Outlet outlet; // 콘센트

	@Enumerated(EnumType.STRING)
	private GroupSeat groupSeat; // 단체석

	@Enumerated(EnumType.STRING)
	private Congestion congestion; // 혼잡도
}
