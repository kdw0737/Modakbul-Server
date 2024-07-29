package com.modakbul.domain.chat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOneToOneChatReq {
	private Long theOtherUserId;
	private Long boardId;
}
