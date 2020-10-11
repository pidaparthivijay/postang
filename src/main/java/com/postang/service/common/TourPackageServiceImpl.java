/**
 * 
 */
package com.postang.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.model.TourPackage;
import com.postang.model.TourPackageRequest;
import com.postang.repo.TourPackageRepository;
import com.postang.repo.TourPackageRequestRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class TourPackageServiceImpl implements TourPackageService, Constants {

	@Autowired
	TourPackageRepository tourPackageRepository;

	@Autowired
	TourPackageRequestRepository tourPackageRequestRepository;

	@Override
	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest) {
		return tourPackageRequestRepository.save(tourPackageRequest);
	}

	@Override
	public TourPackage findTourPackageByTourPackageName(String tourPackageName) {
		return tourPackageRepository.findByTourPackageName(tourPackageName);
	}

	@Override
	public Iterable<TourPackageRequest> getAllTourPackageBookings() {
		return tourPackageRequestRepository.findAll();
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageRepository.save(tourPackage);
	}

	@Override
	public Iterable<TourPackage> viewAllTourPackages() {
		return tourPackageRepository.findAll();
	}

}
