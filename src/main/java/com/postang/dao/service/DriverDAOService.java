/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverDAOService {

	Driver saveDriver(Driver driver);

	List<Driver> getAllDrivers();

	List<Driver> getDriversByLocation(String location);

	Driver getDriverByLicense(String license);
}
