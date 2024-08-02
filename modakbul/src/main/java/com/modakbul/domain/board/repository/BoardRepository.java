package com.modakbul.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.cafe.entity.Cafe;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findAllByCafeAndStatusOrderByCreatedAtDesc(Cafe cafe, BoardStatus status);

	List<Board> findAllByCafeIdAndStatusOrderByCreatedAtDesc(Long cafeId, BoardStatus status);
}
