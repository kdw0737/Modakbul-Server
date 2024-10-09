package com.modakbul.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.review.entity.Review;
import com.modakbul.domain.user.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	void deleteAllByUser(User user);
}
