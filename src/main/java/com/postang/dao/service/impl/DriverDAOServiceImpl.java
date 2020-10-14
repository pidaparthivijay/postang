/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.dao.service.DriverDAOService;
import com.postang.domain.Driver;
import com.postang.repo.DriverRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Repository
@Log4j2
public class DriverDAOServiceImpl implements DriverDAOService {

	@Autowired
	DriverRepository driverRepository;
	@Override
	public Driver saveDriver(Driver driver) {
		return driverRepository.save(driver);
	}

	@Override
	public List<Driver> getAllDrivers() {
		return driverRepository.findAll();
	}

	@Override
	public List<Driver> getDriversByLocation(String location) {
		return driverRepository.findByLocation(location);
	}

	@Override
	public Driver getDriverByLicense(String license) {
		return driverRepository.findByDriverLicense(license);
	}

	@Override
	public List<Driver> findSimilar(String location, Date startDate, Date endDate) {
		return driverRepository.findSimilar(location, startDate, endDate);
	}

}
