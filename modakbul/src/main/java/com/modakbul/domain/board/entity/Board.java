package com.modakbul.domain.board.entity;

import java.time.LocalDateTime;

import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.board.enums.BoardType;
import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Board extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "cafe_waiting_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id")
	private Cafe cafe;

	private int recruitCount; //모집 인원

	private String title;

	private String content;

	@Enumerated(EnumType.STRING)
	private BoardStatus status; // CONTINUE, COMPLETE, DELETED

	@Enumerated(EnumType.STRING)
	private BoardType type; // ONE, REGULAR

	private LocalDateTime meetingDate; // 모임 날짜
}
