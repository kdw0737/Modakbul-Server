package com.modakbul.domain.notification.dto;

import com.modakbul.domain.user.entity.User;
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
public class NotificationDto {
	private Long id;
	private Long boardId;
	private String title;
	private String type;
	private String content;
	private Boolean isRead;
	private String createdAt;
	private User sender;
}
