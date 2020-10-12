/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.TourPackageDAOService;
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.service.TourPackageService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class TourPackageServiceImpl implements TourPackageService, Constants {

	@Autowired
	TourPackageDAOService tourPackageDAOService;

	@Override
	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest) {
		return tourPackageDAOService.saveTourPackageRequest(tourPackageRequest);
	}

	@Override
	public TourPackage findByName(String tourPackageName) {
		return tourPackageDAOService.findTourPackageByTourPackageName(tourPackageName);
	}

	@Override
	public List<TourPackageRequest> getAllTourPackageBookings() {
		return tourPackageDAOService.getAllTourPackageBookings();
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageDAOService.saveTourPackage(tourPackage);
	}

	@Override
	public List<TourPackage> viewAllTourPackages() {
		return tourPackageDAOService.getAllTourPackages();
	}

}
