/**
 * 
 */
package com.postang.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.DriverDAOService;
import com.postang.domain.Driver;
import com.postang.service.DriverService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class DriverServiceImpl implements DriverService, Constants {

	@Autowired
	DriverDAOService driverDAOService;

	@Override
	public Driver saveDriver(Driver driver) {
		return driverDAOService.saveDriver(driver);
	}

	@Override
	public List<Driver> getDriverList() {
		return driverDAOService.getAllDrivers();
	}

	@Override
	public Driver getDriverByLicense(String license) {
		return driverDAOService.getDriverByLicense(license);
	}

	@Override
	public List<Driver> getDriversByLocation(String location) {
		return driverDAOService.getDriversByLocation(location);
	}

	@Override
	public String toggleDeleteDriver(Driver driver) {
		Driver existingDriver = driverDAOService.getDriverByLicense(driver.getDriverLicense());
		driver.setDeleted(YES.equals(existingDriver.getDeleted()) ? NO : YES);
		driver.setUpdatedDate(new Date());
		Driver savedDriver = driverDAOService.saveDriver(driver);
		return YES.equals(savedDriver.getDeleted()) ? DEL_SXS : UN_DEL_SXS;
	}

}
