package com.modakbul.domain.board.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.dto.BoardRequestDto;
import com.modakbul.domain.board.dto.BoardResponseDto;
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

	public Long addBoard(User user, Long cafeId, BoardRequestDto.BoardDto request) {
		Category findCategory = categoryRepository.findByCategoryName(request.getCategoryName())
			.orElseThrow(() -> new BaseException(
				BaseResponseStatus.CATEGORY_NOT_EXIST));

		Cafe findCafe = cafeRepository.findById(cafeId).orElseThrow(() -> new BaseException(
			BaseResponseStatus.CAFE_NOT_EXIST));

		Board addBoard = Board.builder()
			.category(findCategory)
			.cafe(findCafe)
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
		boardRepository.save(addBoard);

		return addBoard.getId();
	}

	public BoardResponseDto.UpdateBoardDto findUpdateBoard(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		return BoardResponseDto.UpdateBoardDto.builder()
			.cafeName(findBoard.getCafe().getName())
			.streetAddress(findBoard.getCafe().getAddress().getStreetAddress())
			.categoryName(findBoard.getCategory().getCategoryName())
			.recruitCount(findBoard.getRecruitCount())
			.meetingDate(findBoard.getMeetingDate())
			.startTime(findBoard.getStartTime())
			.endTime(findBoard.getEndTime())
			.title(findBoard.getTitle())
			.content(findBoard.getContent())
			.build();
	}

	@Transactional
	public void modifyBoard(Long boardId, BoardRequestDto.BoardDto request) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		Category findCategory = categoryRepository.findByCategoryName(request.getCategoryName())
			.orElseThrow(() -> new BaseException(
				BaseResponseStatus.CATEGORY_NOT_EXIST));

		findBoard.update(findCategory, request);
	}

	public BoardResponseDto.MeetingDto findBoards(Long cafeId) {
		Cafe findCafe = cafeRepository.findById(cafeId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_EXIST));
		List<Board> findBoardList = boardRepository.findAllByCafeAndStatusOrderByCreatedAtDesc(findCafe,
			BoardStatus.CONTINUE);

		BoardResponseDto.CafeDto addCafe = BoardResponseDto.CafeDto.builder()
			.cafeName(findCafe.getName())
			.streetAddress(findCafe.getAddress().getStreetAddress())
			.image(findCafe.getImageUrls().get(0))
			.openingHour(findCafe.getOpeningHours())
			.outlet(findCafe.getOutlet())
			.groupSeat(findCafe.getGroupSeat())
			.congestion(findCafe.getCongestion())
			.build();

		List<BoardResponseDto.BoardDto> addBoards = findBoardList.stream()
			.map(findBoard -> {
				int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED);
				return BoardResponseDto.BoardDto.builder()
					.title(findBoard.getTitle())
					.categoryName(findBoard.getCategory().getCategoryName())
					.recruitCount(findBoard.getRecruitCount())
					.currentCount(currentCount)
					.meetingDate(findBoard.getMeetingDate())
					.startTime(findBoard.getStartTime())
					.endTime(findBoard.getEndTime())
					.build();
			}).collect(Collectors.toList());

		return BoardResponseDto.MeetingDto.builder()
			.cafe(addCafe)
			.boards(addBoards)
			.build();
	}

	public BoardResponseDto.BoardDetails findBoard(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED);

		return BoardResponseDto.BoardDetails.builder()
			.cafeImageUrls(findBoard.getCafe().getImageUrls())
			.title(findBoard.getTitle())
			.nickname(findBoard.getUser().getNickname())
			.userImage(findBoard.getUser().getImage())
			.createdAt(findBoard.getCreatedAt())
			.categoryName(findBoard.getCategory().getCategoryName())
			.recruitCount(findBoard.getRecruitCount())
			.currentCount(currentCount)
			.meetingDate(findBoard.getMeetingDate())
			.startTime(findBoard.getStartTime())
			.endTime(findBoard.getEndTime())
			.content(findBoard.getContent())
			.build();
	}

	@Transactional
	public void updateStatusIfDatePassed() {
		LocalDate today = LocalDate.now();
		List<Board> findBoards = boardRepository.findByMeetingDateBeforeAndStatus(today, BoardStatus.CONTINUE);

		for (Board findBoard : findBoards) {
			findBoard.updateStatus(BoardStatus.COMPLETE);
		}
	}
}
