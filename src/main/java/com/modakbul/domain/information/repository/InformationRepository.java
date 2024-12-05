package com.modakbul.domain.information.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.information.entity.Information;
import com.modakbul.domain.user.entity.User;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
	void deleteAllByUser(User user);
}
