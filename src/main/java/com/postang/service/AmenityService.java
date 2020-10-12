/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityService {

	Amenity findAmenityByAmenityName(String amenityName);

	AmenityRequest requestAmenity(AmenityRequest amenityRequest);

	Amenity saveAmenity(Amenity amenity);

	List<Amenity> viewAllAmenities();
}
