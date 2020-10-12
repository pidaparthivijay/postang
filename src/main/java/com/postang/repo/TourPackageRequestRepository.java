/**
 * 
 */
package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.TourPackageRequest;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageRequestRepository extends CrudRepository<TourPackageRequest, Long> {

	TourPackageRequest findByTourPackageName(String tourPackageName);
	Iterable<TourPackageRequest> findByUserId(int userId);

}

