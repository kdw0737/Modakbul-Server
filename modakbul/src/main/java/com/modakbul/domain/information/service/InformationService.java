package com.modakbul.domain.information.service;

import org.springframework.stereotype.Service;

import com.modakbul.domain.information.dto.InformationReqDto;
import com.modakbul.domain.information.entity.Information;
import com.modakbul.domain.information.repository.InformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InformationService {

	private final InformationRepository informationRepository;

	public void createInformation(InformationReqDto request) {
		Information information = Information.builder()
			.name(request.getName())
			.address(request.getLocation())
			.outlet(request.getOutlet())
			.groupSeat(request.getGroupSeat())
			.build();
		informationRepository.save(information);
	}
}
