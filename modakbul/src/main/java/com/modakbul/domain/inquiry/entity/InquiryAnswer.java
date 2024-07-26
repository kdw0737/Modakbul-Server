package com.modakbul.domain.inquiry.entity;

import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class InquiryAnswer extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "inquiry_answer_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    private String content;
}
