package com.modakbul.domain.inquiry.entity;

import com.modakbul.domain.report.enums.InquiryStatus;
import com.modakbul.domain.user.entity.User;
import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Lob
    private String image;

    private InquiryStatus status; //COMPLETE, WAITING, DELETED
}
