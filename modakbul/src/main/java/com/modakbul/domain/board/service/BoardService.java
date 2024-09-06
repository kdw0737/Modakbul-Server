package com.modakbul.domain.board.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.block.repository.BlockRepository;
import com.modakbul.domain.board.dto.BoardDetailsDto;
import com.modakbul.domain.board.dto.BoardDetailsResDto;
import com.modakbul.domain.board.dto.BoardDto;
import com.modakbul.domain.board.dto.BoardReqDto;
import com.modakbul.domain.board.dto.BoardsResDto;
import com.modakbul.domain.board.dto.UpdateBoardDto;
import com.modakbul.domain.board.dto.UpdateBoardResDto;
import com.modakbul.domain.board.dto.UserDto;
import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.board.enums.BoardType;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.cafe.repository.CafeRepository;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.CategoryRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final CategoryRepository categoryRepository;
	private final CafeRepository cafeRepository;
	private final MatchRepository matchRepository;
	private final BlockRepository blockRepository;

	@Transactional
	public void createBoard(User user, Long cafeId, BoardReqDto request) {
		Category findCategory = categoryRepository.findByCategoryName(request.getCategory())
			.orElseThrow(() -> new BaseException(
				BaseResponseStatus.CATEGORY_NOT_EXIST));

		Cafe findCafe = cafeRepository.findById(cafeId).orElseThrow(() -> new BaseException(
			BaseResponseStatus.CAFE_NOT_FOUND));

		Board board = Board.builder()
			.category(findCategory)
			.user(user)
			.recruitCount(request.getRecruitCount())
			.meetingDate(request.getMeetingDate())
			.startTime(request.getStartTime())
			.endTime(request.getEndTime())
			.title(request.getTitle())
			.content(request.getContent())
			.status(BoardStatus.CONTINUE)
			.type(BoardType.ONE)
			.build();
		board.setCafe(findCafe);
		boardRepository.save(board);
	}

	public UpdateBoardResDto getBoardInfoForEdit(Long boardId) {
		Board findBoard = boardRepository.findByBoardIdWithCategory(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		UpdateBoardDto updateBoardDto = UpdateBoardDto.builder()
			.category(findBoard.getCategory().getCategoryName())
			.recruitCount(findBoard.getRecruitCount())
			.meetingDate(findBoard.getMeetingDate())
			.startTime(findBoard.getStartTime())
			.endTime(findBoard.getEndTime())
			.title(findBoard.getTitle())
			.content(findBoard.getContent())
			.build();

		return UpdateBoardResDto.builder()
			.board(updateBoardDto)
			.build();
	}

	@Transactional
	public void updateBoard(Long boardId, BoardReqDto request) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		Category findCategory = categoryRepository.findByCategoryName(request.getCategory())
			.orElseThrow(() -> new BaseException(
				BaseResponseStatus.CATEGORY_NOT_EXIST));

		findBoard.update(findCategory, request);
	}

	public BoardsResDto getBoardList(User user, Long cafeId) {
		List<Long> findBlockerId = blockRepository.findBlockerId(user.getId());
		List<Long> findBlockedId = blockRepository.findBlockedId(user.getId());
		List<Long> blocks = new ArrayList<>(findBlockerId);
		blocks.removeAll(findBlockedId);
		blocks.addAll(findBlockedId);

		Cafe findCafe = cafeRepository.findById(cafeId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));
		List<Board> findBoardList = boardRepository.findAllByCafeIdAndStatusOrderByCreatedAtDesc(findCafe.getId(),
			BoardStatus.CONTINUE, blocks);

		List<BoardDto> boardList = findBoardList.stream()
			.map(findBoard -> {
				int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED) + 1;
				return BoardDto.builder()
					.writerId(findBoard.getUser().getId())
					.id(findBoard.getId())
					.title(findBoard.getTitle())
					.category(findBoard.getCategory().getCategoryName())
					.recruitCount(findBoard.getRecruitCount())
					.currentCount(currentCount)
					.meetingDate(findBoard.getMeetingDate())
					.startTime(findBoard.getStartTime())
					.endTime(findBoard.getEndTime())
					.build();
			}).collect(Collectors.toList());

		return BoardsResDto.builder()
			.boards(boardList)
			.build();
	}

	public BoardDetailsResDto getBoardDetails(Long boardId) {
		Board findBoard = boardRepository.findByBoardIdWithCafeAndCategoryAndUser(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED) + 1;

		UserDto userDto = UserDto.builder()
			.id(findBoard.getUser().getId())
			.nickname(findBoard.getUser().getNickname())
			.image(findBoard.getUser().getImage())
			.build();

		String[] parts = findBoard.getCreatedAt().split(" ");
		String date = parts[0];
		String time = parts[1];

		BoardDetailsDto boardDetailsDto = BoardDetailsDto.builder()
			.title(findBoard.getTitle())
			.createdDate(date)
			.createdTime(time)
			.categoryName(findBoard.getCategory().getCategoryName())
			.recruitCount(findBoard.getRecruitCount())
			.currentCount(currentCount)
			.meetingDate(findBoard.getMeetingDate())
			.startTime(findBoard.getStartTime())
			.endTime(findBoard.getEndTime())
			.content(findBoard.getContent())
			.build();

		return BoardDetailsResDto.builder()
			.images(findBoard.getCafe().getImageUrls())
			.user(userDto)
			.board(boardDetailsDto)
			.build();
	}

	@Transactional
	public void deleteBoard(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED);

		if (currentCount != 0) {
			throw new BaseException(BaseResponseStatus.PARTICIPANT_EXIST);
		}
		findBoard.delete();
	}

	@Transactional
	public void completeBoard(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		findBoard.updateStatus(BoardStatus.COMPLETED);
	}

	@Transactional
	public void updateStatusIfDatePassed() {
		List<Board> findBoards = boardRepository.findByMeetingDateBeforeAndStatus(LocalDate.now(),
			BoardStatus.CONTINUE);

		for (Board findBoard : findBoards) {
			findBoard.updateStatus(BoardStatus.COMPLETED);
		}
	}
}
