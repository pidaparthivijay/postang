/**
 * 
 */
package com.postang.service.common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.model.Amenity;
import com.postang.model.AmenityRequest;
import com.postang.repo.AmenityRepository;
import com.postang.repo.AmenityRequestRepository;

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
		Iterable<Amenity> amenitiesIterable = amenityRepository.findAll();
		return StreamSupport.stream(amenitiesIterable.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public AmenityRequest requestAmenity(AmenityRequest amenityRequest) {
		return amenityRequestRepository.save(amenityRequest);
	}

}
