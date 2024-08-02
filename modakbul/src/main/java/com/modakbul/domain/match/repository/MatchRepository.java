package com.modakbul.domain.match.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long> {
	int countAllByBoardAndMatchStatus(Board board, MatchStatus matchStatus);

	List<Matches> findAllByBoard(Board board);
}
