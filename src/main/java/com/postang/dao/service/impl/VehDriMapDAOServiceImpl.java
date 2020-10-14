/**
 * 
 */
package com.postang.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.dao.service.VehDriMapDAOService;
import com.postang.domain.VehicleDriverMapping;
import com.postang.repo.VehicleDriverMappingRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Repository
public class VehDriMapDAOServiceImpl implements VehDriMapDAOService {

	@Autowired
	VehicleDriverMappingRepository vehicleDriverMappingRepository;

	@Override
	public VehicleDriverMapping saveMapping(VehicleDriverMapping vehicleDriverMapping) {
		return vehicleDriverMappingRepository.save(vehicleDriverMapping);
	}

}
