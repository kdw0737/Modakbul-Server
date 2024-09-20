package com.modakbul.domain.user.dto;

import java.util.List;

import com.modakbul.domain.report.enums.ReportStatus;
import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.domain.user.enums.UserJob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportListResDto {
	private Long userId;
	private String image;
	private String nickname;
	private CategoryName categoryName;
	private UserJob job;
	private ReportStatus status;
}
