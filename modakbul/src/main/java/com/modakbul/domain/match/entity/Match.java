package com.modakbul.domain.match.entity;

import com.modakbul.domain.match.enums.MatchStatus;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Match extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiverId;

    private MatchStatus matchStatus; // PENDING, REJECTED, ACCEPTED, CANCEL
}
