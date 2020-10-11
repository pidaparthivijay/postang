/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Amenity;
import com.postang.model.AmenityRequest;

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
