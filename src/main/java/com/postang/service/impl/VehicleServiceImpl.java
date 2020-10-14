/**
 * 
 */
package com.postang.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.VehicleDAOService;
import com.postang.domain.Vehicle;
import com.postang.service.VehicleService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class VehicleServiceImpl implements VehicleService, Constants {

	@Autowired
	VehicleDAOService vehicleDAOService;

	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		return vehicleDAOService.saveVehicle(vehicle);
	}

	@Override
	public List<Vehicle> getVehicleList() {
		return vehicleDAOService.getAllVehicles();
	}

	@Override
	public Vehicle getVehicleByRegNum(String regNum) {
		return vehicleDAOService.getVehicleByRegNum(regNum);
	}

	@Override
	public List<Vehicle> getVehiclesByLocation(String location) {
		return vehicleDAOService.getVehiclesByLocation(location);
	}

	@Override
	public String toggleDeleteVehicle(Vehicle vehicle) {
		Vehicle existingVehicle = vehicleDAOService.getVehicleByRegNum(vehicle.getRegNum());
		vehicle.setDeleted(YES.equals(existingVehicle.getDeleted()) ? NO : YES);
		vehicle.setUpdatedDate(new Date());
		Vehicle savedVehicle = vehicleDAOService.saveVehicle(vehicle);
		return YES.equals(savedVehicle.getDeleted()) ? DEL_SXS : UN_DEL_SXS;
	}

}
