package com.modakbul.domain.match.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.match.dto.MatchResponseDto;
import com.modakbul.domain.match.service.MatchService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MatchController {

	private final MatchService matchService;

	@PostMapping("/boards/{boardId}/matches")
	public BaseResponse<Map<String, Long>> matchAdd(@AuthenticationPrincipal User user,
		@PathVariable(name = "boardId") Long boardId) {
		Map<String, Long> match = new HashMap<>();
		match.put("match_id", matchService.addMatch(user, boardId));

		return new BaseResponse<>(BaseResponseStatus.CREATE_MATCHING_SUCCESS, match);
	}

	@GetMapping("/boards/{boardId}/matches")
	public BaseResponse<List<MatchResponseDto.MatchListDto>> matchList(@PathVariable(name = "boardId") Long boardId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_MATCH_SUCCESS, matchService.findMatches(boardId));
	}

	@PatchMapping("/matches/{matchesId}/acceptance")
	public BaseResponse<Void> matchModifyAcceptance(@PathVariable(name = "matchesId") Long matchesId) {
		matchService.modifyMatchAcceptance(matchesId);
		return new BaseResponse<>(BaseResponseStatus.APPLICATION_MATCHING_SUCCESS);
	}

	@PatchMapping("/matches/{matchesId}/rejection")
	public BaseResponse<Void> matchModifyRejection(@PathVariable(name = "matchesId") Long matchesId) {
		matchService.modifyMatchRejection(matchesId);
		return new BaseResponse<>(BaseResponseStatus.APPLICATION_MATCHING_SUCCESS);
	}
}
