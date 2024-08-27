package com.modakbul.domain.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.board.dto.BoardDetailsResDto;
import com.modakbul.domain.board.dto.BoardReqDto;
import com.modakbul.domain.board.dto.BoardsResDto;
import com.modakbul.domain.board.dto.UpdateBoardResDto;
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
	public BaseResponse<Map<String, Long>> createBoard(@AuthenticationPrincipal User user,
		@PathVariable(name = "cafeId") Long cafeId,
		@RequestBody BoardReqDto request) {
		Map<String, Long> board = new HashMap<>();
		board.put("board_id", boardService.createBoard(user, cafeId, request));

		return new BaseResponse<>(BaseResponseStatus.CREATE_BOARD_SUCCESS, board);
	}

	@GetMapping("/boards/{boardId}")
	public BaseResponse<UpdateBoardResDto> getBoardInfoForEdit(
		@PathVariable(name = "boardId") Long boardId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_UPDATE_BOARD_SUCCESS,
			boardService.getBoardInfoForEdit(boardId));
	}

	@PatchMapping("/boards/{boardId}")
	public BaseResponse<Void> updateBoard(@PathVariable(name = "boardId") Long boardId,
		@RequestBody BoardReqDto request) {
		boardService.updateBoard(boardId, request);

		return new BaseResponse<>(BaseResponseStatus.UPDATE_BOARD_SUCCESS);
	}

	@GetMapping("/cafes/{cafeId}/boards")
	public BaseResponse<BoardsResDto> getBoardList(@AuthenticationPrincipal User user,
		@PathVariable(name = "cafeId") Long cafeId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_BOARD_LIST_SUCCESS,
			boardService.getBoardList(user, cafeId));
	}

	@GetMapping("/cafes/boards/{boardId}")
	public BaseResponse<BoardDetailsResDto> getBoardDetails(@PathVariable(name = "boardId") Long boardId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_BOARD_SUCCESS, boardService.getBoardDetails(boardId));
	}

	@DeleteMapping("/boards/{boardId}")
	public BaseResponse<Void> deleteBoard(@PathVariable(name = "boardId") Long boardId) {
		boardService.deleteBoard(boardId);
		return new BaseResponse<>(BaseResponseStatus.DELETE_BOARD_SUCCESS);
	}
}
