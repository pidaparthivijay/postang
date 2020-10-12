/**
 * 
 */
package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Amenity;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

	Amenity findByAmenityName(String amenityName);

}
