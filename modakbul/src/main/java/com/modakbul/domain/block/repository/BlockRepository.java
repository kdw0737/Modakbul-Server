package com.modakbul.domain.block.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.block.entity.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
	@Query("select b from Block b "
		+ "where b.blockerId.id = :userId")
	List<Block> findAllByBlockerId(@Param("userId") Long userId);

	@Query("select b.blockedId.id from Block b "
		+ "where b.blockerId.id = :blockerId ")
	List<Long> findBlockedId(@Param("blockerId") Long blockerId);

	@Query("select b.blockerId.id from Block b "
		+ "where b.blockedId.id = :blockedId ")
	List<Long> findBlockerId(@Param("blockedId") Long blockedId);
}
