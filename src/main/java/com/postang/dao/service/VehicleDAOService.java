/**
 * 
 */
package com.postang.dao.service;

import java.util.Date;
import java.util.List;

import com.postang.domain.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleDAOService {

	Vehicle saveVehicle(Vehicle vehicle);

	List<Vehicle> getAllVehicles();

	List<Vehicle> getVehiclesByLocation(String location);

	Vehicle getVehicleByRegNum(String regNum);

	List<Vehicle> findSimilar(Vehicle dummyVehicle, Date startDate, Date endDate);
}
