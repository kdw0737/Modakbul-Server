package com.modakbul.domain.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.board.dto.BoardRequestDto;
import com.modakbul.domain.board.dto.BoardResponseDto;
import com.modakbul.domain.board.service.BoardService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping("/cafes/{cafeId}/boards")
	public BaseResponse<Map<String, Long>> boardAdd(@AuthenticationPrincipal User user,
		@PathVariable(name = "cafeId") Long cafeId,
		@RequestBody BoardRequestDto.BoardDto request) {
		Map<String, Long> board = new HashMap<>();
		board.put("board_id", boardService.addBoard(user, cafeId, request));

		return new BaseResponse<>(BaseResponseStatus.CREATE_BOARD_SUCCESS, board);
	}

	@GetMapping("/boards/{boardId}")
	public BaseResponse<BoardResponseDto.UpdateBoardDto> updateBoardDetails(
		@PathVariable(name = "boardId") Long boardId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_UPDATE_BOARD_SUCCESS,
			boardService.findUpdateBoard(boardId));
	}

	@PatchMapping("/boards/{boardId}")
	public BaseResponse<Void> boardModify(@PathVariable(name = "boardId") Long boardId,
		@RequestBody BoardRequestDto.BoardDto request) {
		boardService.modifyBoard(boardId, request);

		return new BaseResponse<>(BaseResponseStatus.UPDATE_BOARD_SUCCESS);
	}

	@GetMapping("/cafes/{cafeId}/boards")
	public BaseResponse<BoardResponseDto.MeetingDto> boardList(@PathVariable(name = "cafeId") Long cafeId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_BOARD_LIST_SUCCESS,
			boardService.findBoards(cafeId));
	}

	@GetMapping("/cafes/boards/{boardId}")
	public BaseResponse<BoardResponseDto.BoardDetails> boardDetails(@PathVariable(name = "boardId") Long boardId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_BOARD_SUCCESS, boardService.findBoard(boardId));
	}
}
