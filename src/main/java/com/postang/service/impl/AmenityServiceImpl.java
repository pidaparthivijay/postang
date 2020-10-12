/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
import com.postang.repo.AmenityRepository;
import com.postang.repo.AmenityRequestRepository;
import com.postang.service.AmenityService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class AmenityServiceImpl implements AmenityService, Constants {

	@Autowired
	AmenityRequestRepository amenityRequestRepository;

	@Autowired
	AmenityRepository amenityRepository;

	@Override
	public Amenity findAmenityByAmenityName(String amenityName) {
		return amenityRepository.findByAmenityName(amenityName);
	}

	@Override
	public Amenity saveAmenity(Amenity amenity) {
		return amenityRepository.save(amenity);
	}

	@Override
	public List<Amenity> viewAllAmenities() {
		return amenityRepository.findAll();
	}

	@Override
	public AmenityRequest requestAmenity(AmenityRequest amenityRequest) {
		return amenityRequestRepository.save(amenityRequest);
	}

}
