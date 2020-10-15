/**
 * 
 */
package com.postang.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.dao.service.VehDriMapDAOService;
import com.postang.domain.TourPackageRequest;
import com.postang.domain.VehicleDriverMapping;
import com.postang.repo.VehicleDriverMappingRepository;

/**
 * @author Subrahmanya Vijay
 *
 */
@Repository
public class VehDriMapDAOServiceImpl implements VehDriMapDAOService {

	@Autowired
	VehicleDriverMappingRepository vehicleDriverMappingRepository;

	@Override
	public VehicleDriverMapping saveMapping(VehicleDriverMapping vehicleDriverMapping) {
		return vehicleDriverMappingRepository.save(vehicleDriverMapping);
	}

	@Override
	public VehicleDriverMapping getMappingByTourPackageRequestId(TourPackageRequest tourPackageRequest) {
		return vehicleDriverMappingRepository
				.findByTourPackageRequestId(Long.valueOf(tourPackageRequest.getTourPackageRequestId()));
	}

}
