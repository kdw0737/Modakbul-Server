package com.modakbul.domain.cafe.entity;

import com.modakbul.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Cafe extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "cafe_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double longitude;

    private Double latitude;

    @Column(columnDefinition = "TEXT")
    private String image;

    private Boolean isWaiting; // 등록 대기 유무
}
