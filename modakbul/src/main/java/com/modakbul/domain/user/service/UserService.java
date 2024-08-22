package com.modakbul.domain.user.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.block.entity.Block;
import com.modakbul.domain.block.reposiroty.BlockRepository;
import com.modakbul.domain.board.dto.BoardInfoDto;
import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.user.dto.BlockListResDto;
import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyProfileReqDto;
import com.modakbul.domain.user.dto.MyProfileResDto;
import com.modakbul.domain.user.dto.UserCafeResDto;
import com.modakbul.domain.user.dto.UserProfileResDto;
import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.Gender;
import com.modakbul.domain.user.repository.CategoryRepository;
import com.modakbul.domain.user.repository.UserCategoryRepository;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;
import com.modakbul.global.s3.service.S3ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final S3ImageService s3ImageService;
	private final MatchRepository matchRepository;
	private final BlockRepository blockRepository;
	private final BoardRepository boardRepository;

	public MyProfileResDto getMyProfileDetails(User user) {
		List<UserCategory> findUserCategories = userCategoryRepository.findAllByUserIdWithCategory(user.getId());

		List<CategoryName> findCategories = findUserCategories.stream()
			.map(userCategory -> userCategory.getCategory().getCategoryName())
			.collect(Collectors.toList());

		return MyProfileResDto.builder()
			.image(user.getImage())
			.nickname(user.getNickname())
			.isVisible(user.getIsVisible())
			.userJob(user.getUserJob())
			.categories(findCategories)
			.build();
	}

	@Transactional
	public void updateMyProfile(User user, MultipartFile image, MyProfileReqDto request) {
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

		s3ImageService.deleteImageFromS3(user.getImage());

		user.update(s3ImageService.upload(image), request);
		userRepository.save(user);
	}

	public List<UserCafeResDto> getCafesHistory(User user) {
		List<Matches> findMatches = matchRepository.findAllByParticipantIdWithCafe(user.getId(),
			MatchStatus.ACCEPTED, LocalDate.now());

		return findMatches.stream()
			.map(findMatch -> UserCafeResDto.builder()
				.cafeId(findMatch.getBoard().getCafe().getId())
				.name(findMatch.getBoard().getCafe().getName())
				.image(findMatch.getBoard().getCafe().getImageUrls().get(0))
				.streetAddress(findMatch.getBoard().getCafe().getAddress().getStreetAddress())
				.build())
			.collect(Collectors.toList());
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
	public List<BoardInfoDto> getMyBoardHistory(User user) {
		List<Board> findBoardList = boardRepository.findAllByUserIdWithCategory(user.getId());

		return findBoardList.stream()
			.map(findBoard -> {
				Integer currentCount =
					matchRepository.countByBoardIdAndStatus(findBoard.getId(), MatchStatus.ACCEPTED) + 1;
				return BoardInfoDto.builder()
					.title(findBoard.getTitle())
					.boardId(findBoard.getId())
					.categoryName(findBoard.getCategory().getCategoryName())
					.recruitCount(findBoard.getRecruitCount())
					.currentCount(currentCount)
					.dayOfWeek(findBoard.getMeetingDate().getDayOfWeek())
					.startTime(findBoard.getStartTime())
					.endTime(findBoard.getEndTime())
					.boardStatus(findBoard.getStatus())
					.build();
			}).collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public List<BoardInfoDto> getMyMatchesRequestHistory(User user) {
		List<Matches> findAllMatchesRequest = matchRepository.findAllMatchesByUserIdWithBoardDetails(user.getId(),
			false);

		return findAllMatchesRequest.stream()
			.map(findMatches -> {
				Integer currentCount =
					matchRepository.countByBoardIdAndStatus(findMatches.getBoard().getId(), MatchStatus.ACCEPTED) + 1;
				return BoardInfoDto.builder()
					.title(findMatches.getBoard().getTitle())
					.boardId(findMatches.getId())
					.categoryName(findMatches.getBoard().getCategory().getCategoryName())
					.recruitCount(findMatches.getBoard().getRecruitCount())
					.currentCount(currentCount)
					.dayOfWeek(findMatches.getBoard().getMeetingDate().getDayOfWeek())
					.startTime(findMatches.getBoard().getStartTime())
					.endTime(findMatches.getBoard().getEndTime())
					.boardStatus(findMatches.getBoard().getStatus())
					.build();
			}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UserProfileResDto getUserProfile(Long userId) {
		User findUser = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		List<UserCategory> findUserCategories = userCategoryRepository.findUserCategoryWithCategoryByUserId(userId);
		List<CategoryName> findCategoryNames = findUserCategories.stream()
			.map(userCategory -> userCategory.getCategory().getCategoryName())
			.collect(Collectors.toList());

		Gender gender = findUser.getIsVisible() ? findUser.getGender() : Gender.PRIVATE;

		return UserProfileResDto.builder()
			.nickname(findUser.getNickname())
			.gender(gender)
			.userCategory(findCategoryNames)
			.userJob(findUser.getUserJob())
			.build();
	}

	@Transactional(readOnly = true)
	public List<BlockListResDto> getBlockedUserList(User user) { //TODO : 쿼리 최적화 고려
		List<Block> findBlockList = blockRepository.findAllByBlockerId(user.getId());
		List<Long> blockedIds = findBlockList.stream()
			.map(findBlock -> findBlock.getBlockedId().getId())
			.collect(Collectors.toList());

		List<UserCategory> findBlockedUserInfos = userCategoryRepository.findCategoryWithUserByUserIds(blockedIds);

		return findBlockedUserInfos.stream()
			.map(findBlockedUserInfo -> BlockListResDto.builder()
				.image(findBlockedUserInfo.getUser().getImage())
				.nickname(findBlockedUserInfo.getUser().getNickname())
				.categoryName(findBlockedUserInfo.getCategory().getCategoryName())
				.job(findBlockedUserInfo.getUser().getUserJob())
				.build())
			.collect(Collectors.toList());

	}

}
