package com.modakbul.domain.cafe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.modakbul.domain.cafe.dto.CafeListResDto;
import com.modakbul.domain.cafe.entity.Cafe;
import com.modakbul.domain.cafe.repository.CafeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CafeService {

	private final CafeRepository cafeRepository;

	public List<CafeListResDto> getCafeListSortByDistance(double latitude, double longitude) {
		List<Cafe> findCafes = cafeRepository.findAllByDistance(latitude, longitude);

		return findCafes.stream()
			.map(findCafe -> CafeListResDto.builder()
				.id(findCafe.getId())
				.image(findCafe.getImageUrls())
				.name(findCafe.getName())
				.meetingCount(findCafe.getBoards().size())
				.location(findCafe.getAddress())
				.openingHour(findCafe.getOpeningHours())
				.outlet(findCafe.getOutlet())
				.groupSeat(findCafe.getGroupSeat())
				.build())
			.collect(Collectors.toList());
	}

	public List<CafeListResDto> getCafeListSortByMeetingCount(double latitude, double longitude) {
		List<Cafe> findCafes = cafeRepository.findAllByMeetingCount(latitude, longitude);

		return findCafes.stream()
			.map(findCafe -> CafeListResDto.builder()
				.id(findCafe.getId())
				.image(findCafe.getImageUrls())
				.name(findCafe.getName())
				.meetingCount(findCafe.getBoards().size())
				.location(findCafe.getAddress())
				.openingHour(findCafe.getOpeningHours())
				.outlet(findCafe.getOutlet())
				.groupSeat(findCafe.getGroupSeat())
				.build())
			.collect(Collectors.toList());
	}

	public List<CafeListResDto> searchCafeList(String cafeName, double latitude, double longitude) {
		List<Cafe> findCafes = cafeRepository.findAllByDistance(latitude, longitude);

		return findCafes.stream()
			.filter(findCafe -> findCafe.getName().contains(cafeName))
			.map(findCafe -> CafeListResDto.builder()
				.id(findCafe.getId())
				.image(findCafe.getImageUrls())
				.name(findCafe.getName())
				.meetingCount(findCafe.getBoards().size())
				.location(findCafe.getAddress())
				.openingHour(findCafe.getOpeningHours())
				.outlet(findCafe.getOutlet())
				.groupSeat(findCafe.getGroupSeat())
				.build())
			.collect(Collectors.toList());
	}
}
