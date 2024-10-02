package com.modakbul.domain.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.domain.block.entity.Block;
import com.modakbul.domain.block.repository.BlockRepository;
import com.modakbul.domain.board.dto.BoardInfoDto;
import com.modakbul.domain.board.entity.Board;
import com.modakbul.domain.board.repository.BoardRepository;
import com.modakbul.domain.match.entity.Matches;
import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.match.repository.MatchRepository;
import com.modakbul.domain.report.dto.ReportInfo;
import com.modakbul.domain.report.entity.ChatReport;
import com.modakbul.domain.report.entity.UserReport;
import com.modakbul.domain.report.enums.ReportStatus;
import com.modakbul.domain.report.repository.ChatReportRepository;
import com.modakbul.domain.report.repository.UserReportRepository;
import com.modakbul.domain.user.dto.BlockListResDto;
import com.modakbul.domain.user.dto.MeetingsHistoryResDto;
import com.modakbul.domain.user.dto.MyMatchesRequestHistoryDto;
import com.modakbul.domain.user.dto.MyProfileReqDto;
import com.modakbul.domain.user.dto.MyProfileResDto;
import com.modakbul.domain.user.dto.ReportListResDto;
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
	private final UserReportRepository userReportRepository;
	private final ChatReportRepository chatReportRepository;

	public MyProfileResDto getMyProfileDetails(User user) {
		List<UserCategory> findUserCategories = userCategoryRepository.findAllByUserIdWithCategory(user.getId());

		List<CategoryName> findCategories = findUserCategories.stream()
			.map(userCategory -> userCategory.getCategory().getCategoryName())
			.collect(Collectors.toList());

		return MyProfileResDto.builder()
			.id(user.getId())
			.nickname(user.getNickname())
			.image(user.getImage())
			.isGenderVisible(user.getIsVisible())
			.job(user.getUserJob())
			.categories(findCategories)
			.build();
	}

	@Transactional
	public void updateMyProfile(User user, MultipartFile image, MyProfileReqDto request) {
		String imageUrl = user.getImage();

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

		if (!(image == null || image.isEmpty())) {
			if (imageUrl != null) {
				s3ImageService.deleteImageFromS3(imageUrl);
			}
			imageUrl = s3ImageService.upload(image);
		}
		user.update(imageUrl, request);
		userRepository.save(user);
	}

	public List<UserCafeResDto> getCafesHistory(User user) {
		List<Matches> findMatches = matchRepository.findAllByParticipantIdWithCafe(user.getId(),
			MatchStatus.ACCEPTED, LocalDate.now());
		List<Board> findBoards = boardRepository.findAllByUserIdWithCafe(user.getId(), LocalDate.now());

		return Stream.concat(findMatches.stream().map(findMatch -> UserCafeResDto.builder()
				.id(findMatch.getBoard().getCafe().getId())
				.name(findMatch.getBoard().getCafe().getName())
				.image(findMatch.getBoard().getCafe().getImageUrls().get(0))
				.address(findMatch.getBoard().getCafe().getAddress().getStreetAddress())
				.meetingDate(findMatch.getBoard().getMeetingDate())
				.build()
			),
			findBoards.stream().map(findBoard -> UserCafeResDto.builder()
				.id(findBoard.getCafe().getId())
				.name(findBoard.getCafe().getName())
				.image(findBoard.getCafe().getImageUrls().get(0))
				.address(findBoard.getCafe().getAddress().getStreetAddress())
				.meetingDate(findBoard.getMeetingDate())
				.build())).collect(Collectors.toList());
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
				.boardId(board.getId())
				.title(board.getTitle())
				.categoryName(board.getCategory().getCategoryName())
				.meetingDate(board.getMeetingDate())
				.startTime(board.getStartTime())
				.endTime(board.getEndTime())
				.boardStatus(board.getStatus())
				.cafeId(board.getCafe().getId())
				.cafeName(board.getCafe().getName())
				.roadName(board.getCafe().getAddress().getStreetAddress())
				.build()
			),
			findParticipantBoardList.stream().map(match -> {
				Board board = match.getBoard();
				return MeetingsHistoryResDto.builder()
					.boardId(board.getId())
					.title(board.getTitle())
					.categoryName(board.getCategory().getCategoryName())
					.meetingDate(board.getMeetingDate())
					.startTime(board.getStartTime())
					.endTime(board.getEndTime())
					.boardStatus(board.getStatus())
					.cafeId(board.getCafe().getId())
					.cafeName(board.getCafe().getName())
					.roadName(board.getCafe().getAddress().getStreetAddress())
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
					.meetingDate(findBoard.getMeetingDate())
					.startTime(findBoard.getStartTime())
					.endTime(findBoard.getEndTime())
					.boardStatus(findBoard.getStatus())
					.cafeId(findBoard.getCafe().getId())
					.cafeName(findBoard.getCafe().getName())
					.build();
			}).collect(Collectors.toList());

	}

	@Transactional(readOnly = true)
	public List<MyMatchesRequestHistoryDto> getMyMatchesRequestHistory(User user) {
		List<Matches> findAllMatchesRequest = matchRepository.findAllMatchesByUserIdWithBoardDetails(user.getId(),
			false);

		return findAllMatchesRequest.stream()
			.map(findMatches -> {
				Integer currentCount =
					matchRepository.countByBoardIdAndStatus(findMatches.getBoard().getId(), MatchStatus.ACCEPTED) + 1;
				return MyMatchesRequestHistoryDto.builder()
					.title(findMatches.getBoard().getTitle())
					.boardId(findMatches.getBoard().getId())
					.matchId(findMatches.getId())
					.categoryName(findMatches.getBoard().getCategory().getCategoryName())
					.recruitCount(findMatches.getBoard().getRecruitCount())
					.currentCount(currentCount)
					.meetingDate(findMatches.getBoard().getMeetingDate())
					.startTime(findMatches.getBoard().getStartTime())
					.endTime(findMatches.getBoard().getEndTime())
					.boardStatus(findMatches.getBoard().getStatus())
					.matchStatus(findMatches.getMatchStatus())
					.cafeId(findMatches.getBoard().getCafe().getId())
					.cafeName(findMatches.getBoard().getCafe().getName())
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
			.id(findUser.getId())
			.image(findUser.getImage())
			.nickname(findUser.getNickname())
			.gender(gender)
			.categories(findCategoryNames)
			.job(findUser.getUserJob())
			.build();
	}

	@Transactional(readOnly = true)
	public List<BlockListResDto> getBlockedUserList(User user) {
		// Blocker ID로 차단 목록 가져오기
		List<Block> findBlockList = blockRepository.findAllByBlockerId(user.getId());

		// Block된 사용자 ID와 Block ID 매핑 (Map<blockedId, blockId>)
		Map<Long, Long> blockedIdToBlockIdMap = findBlockList.stream()
			.collect(Collectors.toMap(
				block -> block.getBlockedId().getId(),
				Block::getId
			));

		// Block된 사용자 정보 조회
		List<UserCategory> findBlockedUserInfos = userCategoryRepository.findCategoryWithUserByUserIds(
			new ArrayList<>(blockedIdToBlockIdMap.keySet())  // blockedIds 리스트 전달
		);

		// 사용자별로 한 번만 결과를 반환 (첫 번째 카테고리만 포함)
		return findBlockedUserInfos.stream()
			.collect(Collectors.toMap(  // 동일한 사용자 ID는 하나의 항목으로
				findBlockedUserInfo -> findBlockedUserInfo.getUser().getId(),
				findBlockedUserInfo -> BlockListResDto.builder()
					.blockId(blockedIdToBlockIdMap.get(findBlockedUserInfo.getUser().getId()))
					.blockedId(findBlockedUserInfo.getUser().getId())
					.image(findBlockedUserInfo.getUser().getImage())
					.nickname(findBlockedUserInfo.getUser().getNickname())
					.categoryName(findBlockedUserInfo.getCategory().getCategoryName())  // 첫 번째 카테고리만
					.job(findBlockedUserInfo.getUser().getUserJob())
					.build(),
				(existing, replacement) -> existing  // 중복되는 사용자는 기존 값 유지
			))
			.values()
			.stream()
			.collect(Collectors.toList());
	}

	public List<ReportListResDto> getReportedUserList(User user) {
		List<UserReport> findUserReportList = userReportRepository.findByReporterId(user.getId());
		List<ChatReport> findChatReportList = chatReportRepository.findByReporterId(user.getId());

		// UserReport에서 reportedId와 createdAt을 추출하여 리스트 생성
		List<ReportInfo> userReportInfos = findUserReportList.stream()
			.map(report -> new ReportInfo(report.getReported().getId(), report.getCreatedAt()))
			.collect(Collectors.toList());

		// ChatReport에서 reportedId와 createdAt을 추출하여 리스트 생성
		List<ReportInfo> chatReportInfos = findChatReportList.stream()
			.map(report -> new ReportInfo(report.getReported().getId(), report.getCreatedAt()))
			.collect(Collectors.toList());

		// 두 리스트 병합
		List<ReportInfo> allReports = new ArrayList<>();
		allReports.addAll(userReportInfos);
		allReports.addAll(chatReportInfos);

		// createAt을 기준으로 내림차순 정렬 후 reportedId만 추출
		List<Long> reportedIds = allReports.stream()
			.sorted(Comparator.comparing(ReportInfo::getCreatedAt).reversed()) // createAt 내림차순 정렬
			.map(ReportInfo::getReportedId) // reportedId 추출
			.collect(Collectors.toList());

		List<UserCategory> findReportedUserInfos = userCategoryRepository.findCategoryWithUserByUserIds(
			reportedIds);

		return findReportedUserInfos.stream()
			.map(findReportedUserInfo -> ReportListResDto.builder()
				.userId(findReportedUserInfo.getUser().getId())
				.image(findReportedUserInfo.getUser().getImage())
				.nickname(findReportedUserInfo.getUser().getNickname())
				.categoryName(findReportedUserInfo.getCategory().getCategoryName())
				.job(findReportedUserInfo.getUser().getUserJob())
				.status(ReportStatus.PENDING)
				.build())
			.collect(Collectors.toList());
	}
}
