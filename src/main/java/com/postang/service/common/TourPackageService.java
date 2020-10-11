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

	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest);

	public Iterable<TourPackageRequest> getAllTourPackageBookings();

	public TourPackage findTourPackageByTourPackageName(String tourPackageName);

	public TourPackage saveTourPackage(TourPackage tourPackage);

	public Iterable<TourPackage> viewAllTourPackages();

}
