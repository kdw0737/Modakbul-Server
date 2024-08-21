package com.modakbul.domain.block.reposiroty;

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
}
