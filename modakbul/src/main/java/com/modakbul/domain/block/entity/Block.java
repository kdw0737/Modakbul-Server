package com.modakbul.domain.block.entity;

import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Block extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private User blockerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id")
    private User blockedId;
}
