package com.modakbul.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
	List<UserCategory> findUserCategoryByUser(User user);

	void deleteAllByUser(User user);
}
