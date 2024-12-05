package com.modakbul.domain.information.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.information.dto.InformationReqDto;
import com.modakbul.domain.information.service.InformationService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InformationController {

	private final InformationService informationService;

	@PostMapping("/users/cafes/information")
	public BaseResponse<Void> createInformation(@AuthenticationPrincipal User user, @RequestBody InformationReqDto request) {
		informationService.createInformation(user, request);
		return new BaseResponse<>(BaseResponseStatus.CREATE_INFORMATION);
	}
}
