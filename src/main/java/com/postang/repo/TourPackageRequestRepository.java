/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.TourPackageRequest;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageRequestRepository extends JpaRepository<TourPackageRequest, Long> {

	TourPackageRequest findByTourPackageName(String tourPackageName);

	List<TourPackageRequest> findByUserId(int userId);

	TourPackageRequest findByTourPackageRequestId(long tourPackageRequestId);

	List<TourPackageRequest> findByUserName(String userName);

}

