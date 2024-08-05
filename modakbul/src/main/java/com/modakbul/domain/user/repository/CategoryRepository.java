package com.modakbul.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.domain.user.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
