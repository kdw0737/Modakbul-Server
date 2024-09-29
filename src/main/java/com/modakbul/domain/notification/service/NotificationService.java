package com.modakbul.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.modakbul.domain.notification.dto.DeleteNotificationsReqDto;
import com.modakbul.domain.notification.dto.FcmNotificationDto;
import com.modakbul.domain.notification.dto.NotificationListResDto;
import com.modakbul.domain.notification.dto.NotificationReqDto;
import com.modakbul.domain.notification.entity.Notification;
import com.modakbul.domain.notification.repository.NotificationRepository;
import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.repository.UserRepository;
import com.modakbul.global.common.response.BaseException;
import com.modakbul.global.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final FirebaseMessaging firebaseMessaging;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Transactional
	public void sendAndSaveNotification(User user, Long targetId, NotificationReqDto notificationDto) throws
		FirebaseMessagingException {
		User findTargetUser = userRepository.findById(targetId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_EXIST));

		FcmNotificationDto fcmDto = FcmNotificationDto.builder()
			.title(findTargetUser.getNickname())
			.content(notificationDto.getNotificationType() + "," + notificationDto.getBoardTitle())
			.build();

		Message message = Message.builder()
			.setToken(findTargetUser.getFcmToken())
			.setNotification(fcmDto.toNotification())
			.build();

		firebaseMessaging.send(message);

		Notification notification = Notification.builder()
			.user(findTargetUser)
			.title(findTargetUser.getNickname())
			.content(notificationDto.getBoardTitle())
			.type(notificationDto.getNotificationType())
			.isRead(false)
			.build();

		notificationRepository.save(notification);
	}

	@Transactional(readOnly = true)
	public List<NotificationListResDto> getNotificationList(User user) {
		List<Notification> findNotificationList = notificationRepository.findByUserId(user.getId());

		return findNotificationList.stream()
			.map(notification -> NotificationListResDto.builder()
				.id(notification.getId())
				.title(notification.getTitle())
				.type(notification.getType())
				.content(notification.getContent())
				.isRead(notification.getIsRead())
				.createdAt(notification.getCreatedAt())
				.build()).toList();
	}

	@Transactional
	public void deleteNotifications(DeleteNotificationsReqDto deleteNotificationsReqDto) {
		if (deleteNotificationsReqDto.getNotificationIds() != null && !deleteNotificationsReqDto.getNotificationIds().isEmpty()) {
			notificationRepository.deleteAllById(deleteNotificationsReqDto.getNotificationIds());
		}
	}

	@Transactional
	public void readNotification(Long id) {
		Notification findNotification = notificationRepository.findById(id)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOTIFICATION_NOT_EXIST));

		findNotification.readNotification();
	}
}
