package com.modakbul.domain.report.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.report.service.ReportService;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/reports/{reportedId}")
	public BaseResponse<Void> reportUser(@PathVariable Long reportedId) {
		reportService.reportUser(reportedId);
		return new BaseResponse<>(BaseResponseStatus.REPORT_PROFILE_SUCCESS);
	}
}
