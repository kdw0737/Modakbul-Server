package com.modakbul.domain.review.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.review.dto.ReviewReqDto;
import com.modakbul.domain.review.service.ReviewService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/users/cafes/{cafeId}/reviews")
	public BaseResponse<Void> createReview(@PathVariable(name = "cafeId") long cafeId,
		@RequestBody ReviewReqDto request) {
		reviewService.createReview(cafeId, request);
		return new BaseResponse<>(BaseResponseStatus.CREATE_REVIEW);
	}
}
