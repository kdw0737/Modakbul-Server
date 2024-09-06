package com.modakbul.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.report.entity.UserReport;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

}