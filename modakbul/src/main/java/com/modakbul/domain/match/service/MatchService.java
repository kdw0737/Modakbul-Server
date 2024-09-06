package com.modakbul.domain.match.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.dto.MatchesResDto;
import com.modakbul.domain.match.dto.UserDto;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.repository.UserCategoryRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchRepository matchRepository;
	private final BoardRepository boardRepository;
	private final UserCategoryRepository userCategoryRepository;

	public void createMatch(User user, Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		Matches match = Matches.builder()
			.sender(user)
			.board(findBoard)
			.matchStatus(MatchStatus.PENDING)
			.build();
		matchRepository.save(match);
	}

	public List<MatchesResDto> getMatchList(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		List<Matches> findMatchList = matchRepository.findByBoardWithUser(findBoard.getId(), MatchStatus.PENDING);

		return findMatchList.stream()
			.map(findMatch -> {
				List<UserCategory> findUserCategories = userCategoryRepository.findAllByUserIdWithCategory(
					findMatch.getSender().getId());
				List<CategoryName> findCategoryNames = findUserCategories.stream()
					.map(userCategory -> userCategory.getCategory().getCategoryName())
					.toList();

				UserDto userDto = UserDto.builder()
					.id(findMatch.getSender().getId())
					.image(findMatch.getSender().getImage())
					.nickname(findMatch.getSender().getNickname())
					.categories(findCategoryNames)
					.job(findMatch.getSender().getUserJob())
					.build();

				return MatchesResDto.builder()
					.id(findMatch.getId())
					.user(userDto)
					.build();
			}).collect(Collectors.toList());
	}

	@Transactional
	public void updateMatchAcceptance(Long matchesId) {
		Matches findMatch = matchRepository.findById(matchesId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.MATCH_NOT_EXIST));

		findMatch.update(MatchStatus.ACCEPTED);
	}

	@Transactional
	public void updateMatchRejection(Long matchesId) {
		Matches findMatch = matchRepository.findById(matchesId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.MATCH_NOT_EXIST));

		findMatch.update(MatchStatus.REJECTED);
	}

	@Transactional
	public void updateMatchCancel(Long matchesId) {
		Matches findMatch = matchRepository.findById(matchesId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.MATCH_NOT_EXIST));

		findMatch.update(MatchStatus.CANCEL);
	}

	@Transactional
	public void updateMatchExit(Long matchesId) {
		Matches findMatch = matchRepository.findById(matchesId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.MATCH_NOT_EXIST));

		findMatch.update(MatchStatus.EXIT);
	}
}
