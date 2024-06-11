package com.modakbul.domain.chatRoom.entity;

import com.modakbul.domain.chatRoom.enums.UserChatRoomStatus;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class UserChatRoom extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_rooom_id")
    private ChatRoom chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Enumerated(EnumType.STRING)
    private UserChatRoomStatus userChatRoomStatus; // ACTIVE, LEFT ( 사용자가 방에 있는 상태, 방을 떠난 상태 )
}
