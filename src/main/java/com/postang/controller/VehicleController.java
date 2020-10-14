package com.postang.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.Vehicle;
import com.postang.model.RequestDTO;
import com.postang.service.VehicleService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class VehicleController implements RequestMappings, Constants {

	@Autowired
	VehicleService vehicleService;

	@PostMapping(value = VEHICLE_CREATE)
	public RequestDTO createVehicle(@RequestBody RequestDTO requestDTO) {
		Vehicle vehicle = requestDTO.getVehicle();
		log.info("createVehicle starts..." + vehicle);
		try {
			vehicle.setDeleted(NO);
			vehicle.setCreatedDate(new Date());
			Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
			requestDTO.setVehicle(savedVehicle);
			requestDTO.setActionStatus(savedVehicle.getVehicleId() > 0 ? SUCCESS : FAILURE);
		} catch (Exception e) {
			log.error("Exception in createVehicle" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = VEHICLE_UPDATE)
	public RequestDTO updateVehicle(@RequestBody RequestDTO requestDTO) {
		Vehicle vehicle = requestDTO.getVehicle();
		log.info("updateVehicle starts..." + vehicle);
		try {
			vehicle.setUpdatedDate(new Date());
			Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
			requestDTO.setVehicle(savedVehicle);
			requestDTO.setActionStatus(UPDATE_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updateVehicle" + e);
		}
		return requestDTO;
	}

	@PostMapping(value = VEHICLE_DELETE_TOGGLE)
	public RequestDTO toggleDelete(@RequestBody RequestDTO requestDTO) {
		long vehicleId = requestDTO.getVehicle().getVehicleId();
		log.info("toggleDelete starts..." + vehicleId);
		try {
			String toggleStatus = vehicleService.toggleDeleteVehicle(requestDTO.getVehicle());
			return viewVehicleList(toggleStatus);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDelete" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = VEHICLE_VIEW_ALL)
	public RequestDTO viewVehicleList(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewVehicleList starts...");
		try {
			requestDTO.setVehiclesList(vehicleService.getVehicleList());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewVehicleList" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

}
