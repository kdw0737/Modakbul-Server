package com.modakbul.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
