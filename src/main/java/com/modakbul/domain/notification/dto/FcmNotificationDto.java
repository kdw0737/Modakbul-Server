package com.modakbul.domain.notification.dto;

import com.google.firebase.messaging.Notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmNotificationDto {
	private String title;
	private String subtitle;
	private String fcmToken;

	public Notification toNotification() {
		return Notification.builder()
			.setTitle(title)
			.setBody(subtitle)
			.build();
	}
}
