package com.modakbul.domain.match.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.dto.MatchResDto;
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

	public Long createMatch(User user, Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));

		Matches addMatch = Matches.builder()
			.sender(user)
			.board(findBoard)
			.matchStatus(MatchStatus.PENDING)
			.build();
		matchRepository.save(addMatch);

		return addMatch.getId();
	}

	public List<MatchResDto.MatchListDto> getMatchList(Long boardId) {
		Board findBoard = boardRepository.findById(boardId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.BOARD_NOT_FOUND));
		List<Matches> findMatchList = matchRepository.findAllByBoard(findBoard);

		List<MatchResDto.MatchListDto> matchList = new ArrayList<>();

		for (Matches findMatch : findMatchList) {
			List<UserCategory> findUserCategories = userCategoryRepository.findUserCategoryByUser(
				findMatch.getSender());
			List<CategoryName> findCategoryNames = findUserCategories.stream()
				.map(userCategory -> userCategory.getCategory().getCategoryName())
				.toList();

			MatchResDto.MatchListDto match = MatchResDto.MatchListDto.builder()
				.matchId(findMatch.getId())
				.userImage(findMatch.getSender().getImage())
				.nickname(findMatch.getSender().getNickname())
				.categoryName(findCategoryNames)
				.userJob(findMatch.getSender().getUserJob())
				.build();

			matchList.add(match);
		}

		return matchList;
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
}
