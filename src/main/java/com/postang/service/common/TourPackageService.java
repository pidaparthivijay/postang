/**
 * 
 */
package com.postang.service.common;

import com.postang.model.TourPackage;
import com.postang.model.TourPackageRequest;


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
