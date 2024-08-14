package com.modakbul.domain.cafe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.cafe.dto.CafeListResDto;
import com.modakbul.domain.cafe.service.CafeService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CafeController {

	private final CafeService cafeService;

	@GetMapping("/cafes/distance")
	public BaseResponse<List<CafeListResDto>> getCafeListSortByDistance(
		@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_SORT_BY_DISTANCE_SUCCESS,
			cafeService.getCafeListSortByDistance(latitude, longitude));
	}

	@GetMapping("/cafes/meeting")
	public BaseResponse<List<CafeListResDto>> getCafeListSortByMeetingCount(
		@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_SORT_BY_MEETING_COUNT_SUCCESS,
			cafeService.getCafeListSortByMeetingCount(latitude, longitude));
	}

	@GetMapping("/cafes")
	public BaseResponse<List<CafeListResDto>> searchCafeList(
		@RequestParam(value = "name") String cafeName,
		@RequestParam(value = "latitude") double latitude,
		@RequestParam(value = "longitude") double longitude
	) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_CAFE_SUCCESS,
			cafeService.searchCafeList(cafeName, latitude, longitude));
	}
}
