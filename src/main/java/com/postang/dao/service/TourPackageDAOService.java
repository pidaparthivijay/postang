/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageDAOService {

	TourPackageRequest saveTourPackageRequest(TourPackageRequest tourPackageRequest);

	TourPackage findTourPackageByTourPackageName(String tourPackageName);

	List<TourPackageRequest> getAllTourPackageBookings();

	TourPackage saveTourPackage(TourPackage tourPackage);

	List<TourPackage> getAllTourPackages();

}
