package com.modakbul.domain.match.entity;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.user.entity.User;
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
public class Matches extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "matches_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Enumerated(EnumType.STRING)
	private MatchStatus matchStatus; // PENDING, REJECTED, ACCEPTED, CANCEL

	public void update(MatchStatus matchStatus) {
		this.matchStatus = matchStatus;
	}
}
