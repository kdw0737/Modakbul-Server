package com.modakbul.domain.user.entity;

import com.modakbul.domain.user.enums.Gender;
import com.modakbul.domain.user.enums.Provider;
import com.modakbul.domain.user.enums.UserJob;
import com.modakbul.domain.user.enums.UserRole;
import com.modakbul.domain.user.enums.UserStatus;
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
public class User extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Provider provider; // APPLE, KAKAO

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 8)
	private String birth;

	@Column(nullable = false, length = 15, unique = true)
	private String nickname;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender; // MALE, FEMALE

	@Column(name = "is_gender_visible")
	private Boolean isVisible; // default = true

	@Column(columnDefinition = "TEXT")
	private String image;

	@Enumerated(EnumType.STRING)
	private UserJob userJob;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
}
