package com.modakbul.domain.block.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.block.entity.Block;
import com.modakbul.domain.block.repository.BlockRepository;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {

	private final UserRepository userRepository;
	private final BlockRepository blockrepository;
	private final MatchRepository matchRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public Long blockUser(User blocker, Long blockedId) {
		User findBlocked = userRepository.findById(blockedId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		Block block = Block.builder()
			.blockerId(blocker)
			.blockedId(findBlocked)
			.build();

		blockrepository.save(block);

		return block.getId();
	}

	@Transactional
	public void unblockUser(Long blockId) {
		Block findBlock = blockrepository.findById(blockId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BLOCK_NOT_EXIST));
		blockrepository.delete(findBlock);
	}
}
