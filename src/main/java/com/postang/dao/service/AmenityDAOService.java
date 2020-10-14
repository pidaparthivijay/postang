/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityDAOService {

	Amenity findByName(String amenityName);

	AmenityRequest saveAmenityRequest(AmenityRequest amenityRequest);

	Amenity saveAmenity(Amenity amenity);

	List<Amenity> getAllAmenities();

	List<AmenityRequest> getRequestListByUserName(String userName);

	Amenity findByAmenityId(long amenityId);
}
