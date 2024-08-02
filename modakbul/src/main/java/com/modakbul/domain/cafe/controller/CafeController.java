package com.modakbul.domain.cafe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.cafe.dto.CafeResponseDto;
import com.modakbul.domain.cafe.service.CafeService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CafeController {

	private final CafeService cafeService;

	@GetMapping("/cafes")
	public BaseResponse<CafeResponseDto.CafeListDto> cafeList() {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_CAFE_LIST, cafeService.findCafeList());
	}

	@GetMapping("")
	public BaseResponse<CafeResponseDto.CafeListDto> sortCafeMeetingCount() {
		return new BaseResponse<>(BaseResponseStatus.SORT_CAFE_MEETING_COUNT, cafeService.findCafeList());
	}

	@GetMapping("cafes/{cafeId}")
	BaseResponse<List<CafeResponseDto.BoardDto>> meetingList(@PathVariable(name = "cafeId") Long cafeId) {
		return new BaseResponse<>(BaseResponseStatus.SEARCH_MEETING_LIST_SUCCESS, cafeService.findMeeting(cafeId));
	}
}
