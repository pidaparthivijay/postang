/**
 * 
 */
package com.postang.service.common;

import com.postang.model.Amenity;
import com.postang.model.AmenityRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityService {

	public Amenity findAmenityByAmenityName(String amenityName);

	public Amenity saveAmenity(Amenity amenity);

	public Iterable<Amenity> viewAllAmenities();

	public AmenityRequest requestAmenity(AmenityRequest amenityRequest);
}
