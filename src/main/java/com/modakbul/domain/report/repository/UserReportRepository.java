package com.modakbul.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.report.entity.UserReport;
import com.modakbul.domain.user.entity.User;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
	@Query("select u from UserReport  u where u.reporter.id = :reporterId")
	List<UserReport> findByReporterId(@Param("reporterId") long reporterId);

	void deleteAllByReported(User user);

	void deleteAllByReporter(User user);
}
