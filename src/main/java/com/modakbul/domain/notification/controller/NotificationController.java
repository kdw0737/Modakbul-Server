package com.modakbul.domain.notification.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.modakbul.domain.notification.dto.DeleteNotificationsReqDto;
import com.modakbul.domain.notification.dto.NotificationListResDto;
import com.modakbul.domain.notification.dto.NotificationReqDto;
import com.modakbul.domain.notification.service.NotificationService;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponse;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@PostMapping("/notifications/{targetId}")
	public BaseResponse<Void> matchRequestNotification(
		@AuthenticationPrincipal User user,
		@PathVariable("targetId") Long targetId,
		@RequestBody NotificationReqDto notificationDto) {
		try {
			notificationService.sendAndSaveNotification(user, targetId, notificationDto);
		} catch (Exception e) {
			e.printStackTrace();
			new BaseException(BaseResponseStatus.NOTIFICATION_FAILED);
		}
		return new BaseResponse<>(BaseResponseStatus.SEND_NOTIFICATION_SUCCESS);
	}

	@GetMapping("/notifications")
	public BaseResponse<List<NotificationListResDto>> getNotificationList(@AuthenticationPrincipal User user) {
		return new BaseResponse<>(BaseResponseStatus.GET_NOTIFICATION_LIST_SUCCESS,
			notificationService.getNotificationList(user));
	}

	@DeleteMapping("/notifications")
	public BaseResponse<Void> deleteNotifications(@RequestBody DeleteNotificationsReqDto deleteNotificationsReqDto) {
		notificationService.deleteNotifications(deleteNotificationsReqDto);
		return new BaseResponse<>(BaseResponseStatus.DELETE_NOTIFICATION_SUCCESS);
	}

	@PatchMapping("/notifications/{notificationId}")
	public BaseResponse<Void> readNotification(@PathVariable("notificationId") Long id) {
		notificationService.readNotification(id);
		return new BaseResponse<>(BaseResponseStatus.READ_NOTIFICATION_SUCCESS);
	}
}
