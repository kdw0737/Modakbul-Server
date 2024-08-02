package com.modakbul.domain.cafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.enums.BoardStatus;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.cafe.dto.CafeResponseDto;
import com.modakbul.domain.cafe.repository.CafeRepository;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CafeService {

	private final CafeRepository cafeRepository;
	private final BoardRepository boardRepository;
	private final MatchRepository matchRepository;

	public CafeResponseDto.CafeListDto findCafeList() {
		return null;
	}

	public CafeResponseDto.CafeListDto sortCafeMeetingCount() {
		return null;
	}

	public List<CafeResponseDto.BoardDto> findMeeting(Long cafeId) {
		List<Board> findBoardList = boardRepository.findAllByCafeIdAndStatusOrderByCreatedAtDesc(cafeId,
			BoardStatus.CONTINUE);

		List<CafeResponseDto.BoardDto> addBoards = new ArrayList<>();

		for (Board findBoard : findBoardList) {
			int currentCount = matchRepository.countAllByBoardAndMatchStatus(findBoard, MatchStatus.ACCEPTED);

			CafeResponseDto.BoardDto addBoard = CafeResponseDto.BoardDto.builder()
				.title(findBoard.getTitle())
				.categoryName(findBoard.getCategory().getCategoryName())
				.recruitCount(findBoard.getRecruitCount())
				.currentCount(currentCount)
				.meetingDate(findBoard.getMeetingDate())
				.startTime(findBoard.getStartTime())
				.endTime(findBoard.getEndTime())
				.build();
			addBoards.add(addBoard);
		}

		return addBoards;
	}

}
