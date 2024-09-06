package com.modakbul.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.enums.Provider;
import com.modakbul.domain.user.enums.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndProvider(String email, Provider provider);

	Optional<User> findByNickname(String nickname);

	Optional<User> findByIdAndUserStatus(Long userId, UserStatus active);

}
