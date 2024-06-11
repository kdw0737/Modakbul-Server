package com.modakbul.domain.chatMessage.entity;

import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class chatMessage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
