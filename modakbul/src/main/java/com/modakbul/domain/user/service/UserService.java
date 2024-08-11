package com.modakbul.domain.user.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyBoardHistoryResDto;
import com.modakbul.domain.user.dto.UserRequestDto;
import com.modakbul.domain.user.dto.UserResponseDto;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.repository.CategoryRepository;
import com.modakbul.domain.user.repository.UserCategoryRepository;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final MatchRepository matchRepository;

	public UserResponseDto.ProfileDto findProfile(User user) {
		List<UserCategory> findUserCategories = userCategoryRepository.findCategoryByUser(user);

		List<CategoryName> findCategories = findUserCategories.stream()
			.map(userCategory -> userCategory.getCategory().getCategoryName())
			.collect(Collectors.toList());

		return UserResponseDto.ProfileDto.builder()
			.image(user.getImage())
			.nickname(user.getNickname())
			.isVisible(user.getIsVisible())
			.userJob(user.getUserJob())
			.categories(findCategories)
			.build();
	}

	@Transactional
	public void modifyProfile(User user, UserRequestDto.ProfileDto request) {
		userCategoryRepository.deleteAllByUser(user);

		List<UserCategory> userCategories = request.getCategories().stream()
			.map(categoryName -> {
				Category findCategory = categoryRepository.findByCategoryName(categoryName)
					.orElseThrow(() -> new BaseException(BaseResponseStatus.CATEGORY_NOT_EXIST));
				return UserCategory.builder()
					.user(user)
					.category(findCategory)
					.build();
			}).collect(Collectors.toList());
		userCategoryRepository.saveAll(userCategories);

		user.update(request);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public List<MeetingsHistoryResDto> getMeetingsHistory(User user) {
		List<Board> findOwnBoardList = boardRepository.findAllByUserIdWithCategory(user.getId());
		List<Matches> findParticipantBoardList = matchRepository.findAllByParticipantIdWithBoard(user.getId(),
			MatchStatus.ACCEPTED);
		if (findOwnBoardList.isEmpty() && findParticipantBoardList.isEmpty()) {
			return Collections.emptyList();
		}

		return Stream.concat(
			findOwnBoardList.stream().map(board -> MeetingsHistoryResDto.builder()
				.title(board.getTitle())
				.cafeName(board.getCafe().getName())
				.roadName(board.getCafe().getAddress().getStreetAddress())
				.categoryName(board.getCategory().getCategoryName())
				.meetingDate(board.getMeetingDate())
				.startTime(board.getStartTime())
				.endTime(board.getEndTime())
				.boardStatus(board.getStatus())
				.build()
			),
			findParticipantBoardList.stream().map(match -> {
				Board board = match.getBoard();
				return MeetingsHistoryResDto.builder()
					.title(board.getTitle())
					.cafeName(board.getCafe().getName())
					.roadName(board.getCafe().getAddress().getStreetAddress())
					.categoryName(board.getCategory().getCategoryName())
					.meetingDate(board.getMeetingDate())
					.startTime(board.getStartTime())
					.endTime(board.getEndTime())
					.boardStatus(board.getStatus())
					.build();
			})
		).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<MyBoardHistoryResDto> getMyBoardHistory(User user) {
		List<Board> findBoardList = boardRepository.findAllByUserIdWithCategory(user.getId());
		Integer currentCount = matchRepository.countByUserIdAndStatus(user.getId(), MatchStatus.ACCEPTED) + 1;

		return findBoardList.stream()
			.map(findBoard -> MyBoardHistoryResDto.builder()
				.title(findBoard.getTitle())
				.boardId(findBoard.getId())
				.categoryName(findBoard.getCategory().getCategoryName())
				.recruitCount(findBoard.getRecruitCount())
				.currentCount(currentCount)
				.dayOfWeek(findBoard.getMeetingDate().getDayOfWeek())
				.startTime(findBoard.getStartTime())
				.endTime(findBoard.getEndTime())
				.boardStatus(findBoard.getStatus())
				.build()).collect(Collectors.toList());
	}
}
