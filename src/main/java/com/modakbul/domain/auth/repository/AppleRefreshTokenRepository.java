package com.modakbul.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.auth.entity.AppleRefreshToken;

@Repository
public interface AppleRefreshTokenRepository extends CrudRepository<AppleRefreshToken, Long> {
	//Optional<String> findRefreshTokenById(Long id);
}
