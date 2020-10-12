/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.AmenityDAOService;
import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
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
	AmenityDAOService amenityDAOService;

	@Override
	public Amenity findAmenityByAmenityName(String amenityName) {
		return amenityDAOService.findByName(amenityName);
	}

	@Override
	public Amenity saveAmenity(Amenity amenity) {
		return amenityDAOService.saveAmenity(amenity);
	}

	@Override
	public List<Amenity> viewAllAmenities() {
		return amenityDAOService.getAllAmenities();
	}

	@Override
	public AmenityRequest requestAmenity(AmenityRequest amenityRequest) {
		return amenityDAOService.saveAmenityRequest(amenityRequest);
	}

}
