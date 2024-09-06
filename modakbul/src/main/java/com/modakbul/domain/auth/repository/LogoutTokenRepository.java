package com.modakbul.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.auth.entity.LogoutToken;

@Repository
public interface LogoutTokenRepository extends CrudRepository<LogoutToken, String> {
}
