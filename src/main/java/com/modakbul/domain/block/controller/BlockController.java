package com.modakbul.domain.block.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.block.service.BlockService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BlockController {

	private final BlockService blockService;

	@PostMapping("/blocks/{userId}")
	public BaseResponse<Long> blockUser(@AuthenticationPrincipal User user, @PathVariable("userId") Long blockedId) {
		return new BaseResponse<>(BaseResponseStatus.BLOCK_SUCCESS, blockService.blockUser(user, blockedId));
	}

	@DeleteMapping("/blocks/{blockId}")
	public BaseResponse<Void> unblockUser(@PathVariable Long blockId) {
		blockService.unblockUser(blockId);
		return new BaseResponse<>(BaseResponseStatus.UNBLOCK_SUCCESS);
	}
}
