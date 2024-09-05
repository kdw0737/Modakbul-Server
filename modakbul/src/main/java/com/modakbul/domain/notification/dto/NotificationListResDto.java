package com.modakbul.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResDto {
	private Long id;
	private String title;
	private String type;
	private String content;
	private Boolean isRead;
	private String createdAt;
}
