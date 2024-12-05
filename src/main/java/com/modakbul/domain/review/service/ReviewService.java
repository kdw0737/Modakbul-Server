package com.modakbul.domain.review.service;

import org.springframework.stereotype.Service;

import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.cafe.repository.CafeRepository;
import com.modakbul.domain.review.dto.ReviewReqDto;
import com.modakbul.domain.review.entity.Review;
import com.modakbul.domain.review.repository.ReviewRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final CafeRepository cafeRepository;

	public void createReview(User user, long cafeId, ReviewReqDto request) {
		Cafe findCafe = cafeRepository.findById(cafeId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.CAFE_NOT_FOUND));

		Review review = Review.builder()
			.cafe(findCafe)
			.outlet(request.getOutlet())
			.groupSeat(request.getGroupSeat())
			.user(user)
			.build();
		reviewRepository.save(review);
	}
}
