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
import com.postang.domain.Driver;
import com.postang.model.RequestDTO;
import com.postang.service.DriverService;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class DriverController implements RequestMappings, Constants {

	@Autowired
	DriverService driverService;

	@PostMapping(value = DRIVER_CREATE)
	public RequestDTO createDriver(@RequestBody RequestDTO requestDTO) {
		Driver driver = requestDTO.getDriver();
		try {
			driver.setDeleted(NO);
			driver.setCreatedDate(new Date());
			Driver savedDriver = driverService.saveDriver(driver);
			requestDTO.setDriver(savedDriver);
			requestDTO.setActionStatus(savedDriver.getDriverId() > 0 ? SUCCESS : FAILURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = DRIVER_UPDATE)
	public RequestDTO updateDriver(@RequestBody RequestDTO requestDTO) {
		Driver driver = requestDTO.getDriver();
		try {
			driver.setUpdatedDate(new Date());
			Driver savedDriver = driverService.saveDriver(driver);
			requestDTO.setDriver(savedDriver);
			requestDTO.setActionStatus(UPDATE_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = DRIVER_DELETE_TOGGLE)
	public RequestDTO toggleDelete(@RequestBody RequestDTO requestDTO) {
		try {
			String toggleStatus = driverService.toggleDeleteDriver(requestDTO.getDriver());
			return viewDriverList(toggleStatus);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = DRIVER_VIEW_ALL)
	public RequestDTO viewDriverList(String status) {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setDriversList(driverService.getDriverList());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

}
