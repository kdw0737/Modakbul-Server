package com.modakbul.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.user.entity.Category;
import com.modakbul.domain.user.enums.CategoryName;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryName(CategoryName categoryName);
}
