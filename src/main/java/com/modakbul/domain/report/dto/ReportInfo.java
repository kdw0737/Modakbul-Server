package com.modakbul.domain.report.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportInfo {
	private Long reportedId;
	private LocalDateTime createdAt; // LocalDateTime 타입으로 변경

	public ReportInfo(Long reportedId, String createdAtString) {
		this.reportedId = reportedId;
		this.createdAt = parseToLocalDateTime(createdAtString); // 문자열을 LocalDateTime으로 변환
	}

	private LocalDateTime parseToLocalDateTime(String dateTimeString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(dateTimeString, formatter);
	}
}
