/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.constants.Constants;
import com.postang.dao.service.AmenityDAOService;
import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
import com.postang.repo.AmenityRepository;
import com.postang.repo.AmenityRequestRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Repository
public class AmenityDAOServiceImpl implements AmenityDAOService, Constants {

	@Autowired
	AmenityRequestRepository amenityRequestRepository;

	@Autowired
	AmenityRepository amenityRepository;

	@Override
	public Amenity findByName(String amenityName) {
		return amenityRepository.findByAmenityName(amenityName);
	}

	@Override
	public Amenity saveAmenity(Amenity amenity) {
		return amenityRepository.save(amenity);
	}

	@Override
	public List<Amenity> getAllAmenities() {
		return amenityRepository.findAll();
	}

	@Override
	public AmenityRequest saveAmenityRequest(AmenityRequest amenityRequest) {
		return amenityRequestRepository.save(amenityRequest);
	}

	@Override
	public List<AmenityRequest> getRequestListByUserName(String userName) {
		return amenityRequestRepository.findByUserName(userName);
	}

	@Override
	public Amenity findByAmenityId(long amenityId) {
		return amenityRepository.findByAmenityId(amenityId);
	}

}
