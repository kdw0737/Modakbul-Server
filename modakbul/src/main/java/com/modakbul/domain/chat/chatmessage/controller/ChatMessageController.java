package com.modakbul.domain.chat.chatmessage.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.modakbul.domain.chat.chatmessage.dto.MessageDto;
import com.modakbul.domain.chat.chatmessage.service.ChatMessageService;
import com.modakbul.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/message")
	public void sendMessage(@RequestBody MessageDto messageDto, @AuthenticationPrincipal User user) {
		chatMessageService.sendMessage(messageDto, user);
	}
}
