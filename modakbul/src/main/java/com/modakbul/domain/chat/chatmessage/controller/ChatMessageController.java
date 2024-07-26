package com.modakbul.domain.chat.chatmessage.controller;

import org.springframework.stereotype.Controller;

import com.modakbul.domain.chat.chatmessage.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

}
