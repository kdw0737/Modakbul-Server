package com.modakbul.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.cafe.entity.Cafe;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findAllByCafeAndStatusOrderByCreatedAtDesc(Cafe cafe, BoardStatus status);

	List<Board> findAllByCafeIdAndStatusOrderByCreatedAtDesc(Long cafeId, BoardStatus status);

	@Query("SELECT DISTINCT b FROM Board b "
		+ "JOIN FETCH b.cafe c "
		+ "JOIN FETCH b.category cat "
		+ "WHERE b.user.id = :userId ")
	List<Board> findAllByUserIdWithCategory(@Param("userId") Long userId);
}
