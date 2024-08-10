package com.modakbul.domain.match.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Matches, Long> {
	int countAllByBoardAndMatchStatus(Board board, MatchStatus matchStatus);

	List<Matches> findAllByBoard(Board board);

	@Query("SELECT m FROM Matches m "
		+ "JOIN FETCH m.board b "
		+ "JOIN FETCH b.cafe c "
		+ "JOIN FETCH b.category cat "
		+ "WHERE m.sender.id = :userId "
		+ "AND m.matchStatus = :status")
	List<Matches> findAllByParticipantIdWithBoard(@Param("userId") Long userId, @Param("status") MatchStatus status);

	@Query("SELECT COUNT(m) FROM Matches m "
		+ "WHERE m.sender.id = :userId "
		+ "AND m.matchStatus = :status")
	Integer countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") MatchStatus status);
}