/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.domain.VehicleDriverMapping;
import com.postang.model.RequestDTO;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageService {

	TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest);

	TourPackage findByName(String tourPackageName);

	List<TourPackageRequest> getAllTourPackageBookings();

	TourPackage saveTourPackage(TourPackage tourPackage);

	List<TourPackage> viewAllTourPackages();

	RequestDTO viewFeasibleVehiclesDrivers(TourPackageRequest tourPackageRequest);

	String assignVehDriTour(VehicleDriverMapping vehicleDriverMapping);

}
