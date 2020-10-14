/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverService {

	Driver saveDriver(Driver driver);

	List<Driver> getDriverList();

	String toggleDeleteDriver(Driver driver);

	Driver getDriverByLicense(String license);

	List<Driver> getDriversByLocation(String location);
}
