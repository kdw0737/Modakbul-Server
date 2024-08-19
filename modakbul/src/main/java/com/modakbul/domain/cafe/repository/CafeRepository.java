package com.modakbul.domain.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modakbul.domain.cafe.entity.Cafe;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

	@Query(value = "SELECT *, "
		+ "(6371 * ACOS(COS(RADIANS(:userLatitude)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(:userLongitude)) + SIN(RADIANS(:userLatitude)) * SIN(RADIANS(latitude)))) AS distance "
		+ "FROM cafe "
		+ "HAVING distance <= 2 "
		+ "ORDER BY distance;",
		nativeQuery = true)
	List<Cafe> findAllByDistance(@Param("userLatitude") double userLatitude,
		@Param("userLongitude") double userLongitude);

	@Query(value = "SELECT *, "
		+ "(6371 * ACOS(COS(RADIANS(:userLatitude)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(:userLongitude)) + SIN(RADIANS(:userLatitude)) * SIN(RADIANS(latitude)))) AS distance "
		+ "FROM cafe "
		+ "HAVING distance <= 2 "
		+ "ORDER BY meeting_count DESC, distance ASC;",
		nativeQuery = true)
	List<Cafe> findAllByMeetingCount(@Param("userLatitude") double userLatitude,
		@Param("userLongitude") double userLongitude);
}
