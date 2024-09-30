package com.modakbul.domain.chat.chatroom.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.modakbul.domain.user.enums.CategoryName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMessageHistoryResDto {
	private List<String> contents;
	private List<LocalDateTime> sendTimes;
	private String cafeName;
	private String boardTitle;
	private CategoryName categoryName;
}
