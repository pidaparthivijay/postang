/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleService {

	Vehicle saveVehicle(Vehicle vehicle);

	List<Vehicle> getVehicleList();

	String toggleDeleteVehicle(Vehicle vehicle);

	List<Vehicle> getVehiclesByLocation(String location);

	Vehicle getVehicleByRegNum(String regNum);
}
