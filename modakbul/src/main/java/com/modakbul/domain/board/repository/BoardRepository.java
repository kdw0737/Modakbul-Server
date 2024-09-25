package com.modakbul.domain.board.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.cafe.entity.Cafe;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	@Query("SELECT b From Board b "
		+ "JOIN FETCH b.category c "
		+ "WHERE b.cafe.id = :cafeId "
		+ "AND b.status = :status "
		+ "AND b.user.id NOT IN (:blocks) "
		+ "ORDER By b.createdAt DESC")
	List<Board> findAllByCafeIdAndStatusOrderByCreatedAtDesc(@Param("cafeId") Long cafeId,
		@Param("status") BoardStatus status, @Param("blocks") List<Long> blocks);

	@Query("SELECT DISTINCT b FROM Board b "
		+ "JOIN FETCH b.cafe c "
		+ "JOIN FETCH b.category cat "
		+ "WHERE b.user.id = :userId ")
	List<Board> findAllByUserIdWithCategory(@Param("userId") Long userId);

	List<Board> findByMeetingDateBeforeAndStatus(LocalDate today, BoardStatus status);

	@Query("SELECT DISTINCT b FROM Board b "
		+ "JOIN FETCH b.category cat "
		+ "WHERE b.id = :boardId ")
	Optional<Board> findByBoardIdWithCategory(@Param("boardId") Long boardId);

	@Query("SELECT DISTINCT b FROM Board b "
		+ "JOIN FETCH b.cafe c "
		+ "JOIN FETCH b.category cat "
		+ "JOIN FETCH b.user u "
		+ "WHERE b.id = :boardId ")
	Optional<Board> findByBoardIdWithCafeAndCategoryAndUser(@Param("boardId") Long boardId);

	@Query("SELECT b FROM Board b "
		+ "JOIN FETCH b.user u "
		+ "WHERE b.id = :boardId")
	Optional<Board> findByBoardIdwithUser(@Param("boardId") Long boardId);

	List<Board> findAllByCafe(Cafe cafe);
}
