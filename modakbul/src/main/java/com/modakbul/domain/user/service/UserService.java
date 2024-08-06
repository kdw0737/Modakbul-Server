package com.modakbul.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.modakbul.global.s3.service.S3ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserCategoryRepository userCategoryRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final S3ImageService s3ImageService;

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
	public void modifyProfile(User user, MultipartFile image, UserRequestDto.ProfileDto request) {
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
}
