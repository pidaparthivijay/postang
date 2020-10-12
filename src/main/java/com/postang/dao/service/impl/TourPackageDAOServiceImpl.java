/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.constants.Constants;
import com.postang.dao.service.TourPackageDAOService;
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.repo.TourPackageRepository;
import com.postang.repo.TourPackageRequestRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Repository
public class TourPackageDAOServiceImpl implements TourPackageDAOService, Constants {

	@Autowired
	TourPackageRepository tourPackageRepository;

	@Autowired
	TourPackageRequestRepository tourPackageRequestRepository;

	@Override
	public TourPackageRequest saveTourPackageRequest(TourPackageRequest tourPackageRequest) {
		return tourPackageRequestRepository.save(tourPackageRequest);
	}

	@Override
	public TourPackage findTourPackageByTourPackageName(String tourPackageName) {
		return tourPackageRepository.findByTourPackageName(tourPackageName);
	}

	@Override
	public List<TourPackageRequest> getAllTourPackageBookings() {
		return tourPackageRequestRepository.findAll();
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageRepository.save(tourPackage);
	}

	@Override
	public List<TourPackage> getAllTourPackages() {
		return tourPackageRepository.findAll();
	}

}