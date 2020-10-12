/**
 * 
 */
package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.Amenity;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityRepository extends CrudRepository<Amenity, Long> {

	Amenity findByAmenityName(String amenityName);

}
