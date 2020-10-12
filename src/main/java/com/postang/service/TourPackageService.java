/**
 * 
 */
package com.postang.service;

import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageService {

	TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest);

	TourPackage findTourPackageByTourPackageName(String tourPackageName);

	Iterable<TourPackageRequest> getAllTourPackageBookings();

	TourPackage saveTourPackage(TourPackage tourPackage);

	Iterable<TourPackage> viewAllTourPackages();

}
