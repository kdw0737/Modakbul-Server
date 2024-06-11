package com.modakbul.domain.chatRoom.entity;

import com.modakbul.domain.chatRoom.enums.ChatRoomStatus;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    private Integer userCount; // 방 인원

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus; // ACTIVE, DELETED
}
