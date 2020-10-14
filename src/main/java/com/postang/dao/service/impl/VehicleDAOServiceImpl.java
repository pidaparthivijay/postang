/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.dao.service.VehicleDAOService;
import com.postang.domain.Vehicle;
import com.postang.repo.VehicleRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Repository
public class VehicleDAOServiceImpl implements VehicleDAOService {

	@Autowired
	VehicleRepository vehicleRepository;

	@Override
	public Vehicle saveVehicle(Vehicle vehicle) {
		return vehicleRepository.save(vehicle);
	}

	@Override
	public List<Vehicle> getAllVehicles() {
		return vehicleRepository.findAll();
	}

	@Override
	public List<Vehicle> getVehiclesByLocation(String location) {
		return vehicleRepository.findByLocation(location);
	}

	@Override
	public Vehicle getVehicleByRegNum(String regNum) {
		return vehicleRepository.findByRegNum(regNum);
	}

}
