package com.modakbul.domain.board.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.modakbul.domain.board.dto.BoardReqDto;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.board.enums.BoardType;
import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id")
	private Cafe cafe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private int recruitCount; //모집 인원

	private String title;

	private String content;

	@Enumerated(EnumType.STRING)
	private BoardStatus status; // CONTINUE, COMPLETE, DELETED

	@Enumerated(EnumType.STRING)
	private BoardType type; // ONE, REGULAR

	private LocalDate meetingDate; // 모임 날짜

	private LocalTime startTime; // 모임 시작 시간

	private LocalTime endTime; // 모임 종료 시간

	public void update(Category category, BoardReqDto request) {
		this.category = category;
		this.recruitCount = request.getRecruitCount();
		this.title = request.getTitle();
		this.content = request.getContent();
		this.meetingDate = request.getMeetingDate();
		this.startTime = request.getStartTime();
		this.endTime = request.getEndTime();
	}

	public void setCafe(Cafe cafe) {
		this.cafe = cafe;
		cafe.getBoards().add(this);
	}

	public void updateStatus(BoardStatus boardStatus) {
		this.status = boardStatus;
	}

	public void delete() {
		this.status = BoardStatus.DELETED;
		this.cafe = null;
	}
}
