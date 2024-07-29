package com.modakbul.domain.chat.chatmessage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modakbul.domain.chat.chatmessage.repository.ChatMessageRepository;
import com.modakbul.domain.chat.chatroom.repository.ChatRoomRepository;
import com.modakbul.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
}
