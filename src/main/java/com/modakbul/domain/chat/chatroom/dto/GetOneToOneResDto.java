package com.modakbul.domain.chat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetOneToOneResDto {
	private Long roomId;
	private String lastMessage;
}
