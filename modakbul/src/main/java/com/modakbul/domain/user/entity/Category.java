package com.modakbul.domain.user.entity;

import com.modakbul.domain.user.enums.CategoryName;
import com.modakbul.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class Category extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "category_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private CategoryName categoryName;
}
