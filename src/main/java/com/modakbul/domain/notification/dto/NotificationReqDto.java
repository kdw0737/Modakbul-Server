package com.modakbul.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReqDto {
	private Long boardId;
	private String title;
	private String subtitle;
	private String type;
}
