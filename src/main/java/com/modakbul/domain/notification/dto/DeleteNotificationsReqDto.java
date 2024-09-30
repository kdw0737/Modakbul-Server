package com.modakbul.domain.notification.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class DeleteNotificationsReqDto {
	private List<Long> notificationIds;
}
