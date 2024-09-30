package com.modakbul.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.user.entity.User;
import com.modakbul.domain.user.entity.UserCategory;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

	@Query("SELECT DISTINCT uc FROM UserCategory uc "
		+ "JOIN FETCH uc.category c "
		+ "WHERE uc.user.id = :userId ")
	List<UserCategory> findAllByUserIdWithCategory(@Param("userId") Long userId);

	void deleteAllByUser(User user);

	@Query("select uc "
		+ "from UserCategory uc "
		+ "join fetch uc.category c "
		+ "where uc.user.id = :userId")
	List<UserCategory> findUserCategoryWithCategoryByUserId(@Param("userId") Long userId);

	@Query("select uc "
		+ "from UserCategory uc "
		+ "join fetch uc.category c "
		+ "join fetch uc.user u "
		+ "where uc.user.id in :userIds")
	List<UserCategory> findCategoryWithUserByUserIds(@Param("userIds") List<Long> userIds);

}
