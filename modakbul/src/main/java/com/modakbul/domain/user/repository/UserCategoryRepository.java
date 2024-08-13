package com.modakbul.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;
import com.modakbul.domain.user.enums.CategoryName;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
	List<UserCategory> findCategoryByUser(User user);

	void deleteAllByUser(User user);

	List<CategoryName> findCategoryNamesByUser(User user);

	@Query("select uc "
		+ "from UserCategory uc "
		+ "join fetch uc.category c "
		+ "where uc.user.id = :userId")
	List<UserCategory> findUserCategoryWithCategoryByUserId(@Param("userId") Long userId);
}
