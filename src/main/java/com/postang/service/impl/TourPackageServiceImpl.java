/**
 * 
 */
package com.postang.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.dao.service.DriverDAOService;
import com.postang.dao.service.TourPackageDAOService;
import com.postang.dao.service.VehDriMapDAOService;
import com.postang.dao.service.VehicleDAOService;
import com.postang.domain.Customer;
import com.postang.domain.Driver;
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.domain.User;
import com.postang.domain.Vehicle;
import com.postang.domain.VehicleDriverMapping;
import com.postang.model.MailDTO;
import com.postang.model.RequestDTO;
import com.postang.service.TourPackageService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@Service
public class TourPackageServiceImpl implements TourPackageService, Constants {

	@Autowired
	TourPackageDAOService tourPackageDAOService;

	@Autowired
	VehicleDAOService vehicleDAOService;

	@Autowired
	DriverDAOService driverDAOService;

	@Autowired
	VehDriMapDAOService vehDriMapDAOService;

	@Autowired
	CommonDAOService commonDAOService;

	Util util = new Util();

	MailUtil mailUtil = new MailUtil();

	@Override
	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest) {
		return tourPackageDAOService.saveTourPackageRequest(tourPackageRequest);
	}

	@Override
	public TourPackage findByName(String tourPackageName) {
		return tourPackageDAOService.findTourPackageByTourPackageName(tourPackageName);
	}

	@Override
	public List<TourPackageRequest> getAllTourPackageBookings() {
		return tourPackageDAOService.getAllTourPackageBookings();
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageDAOService.saveTourPackage(tourPackage);
	}

	@Override
	public List<TourPackage> viewAllTourPackages() {
		return tourPackageDAOService.getAllTourPackages();
	}

	@Override
	public RequestDTO viewFeasibleVehiclesDrivers(TourPackageRequest tourPackageRequest) {

		RequestDTO requestDTO = new RequestDTO();
		TourPackage tourPackage = tourPackageDAOService
				.findTourPackageByTourPackageName(tourPackageRequest.getTourPackageName());
		Vehicle dummyVehicle = new Vehicle();
		dummyVehicle.setLocation(tourPackage.getLocation());
		dummyVehicle.setVehicleType(this.findVehicleType(tourPackageRequest.getGuestCount()));
		Date startDate = tourPackageRequest.getStartDate();
		Date endDate = util.calculateEndDate(startDate, tourPackage.getDuration());
		List<Vehicle> vehiclesList = vehicleDAOService.findSimilar(dummyVehicle, startDate, endDate);
		List<Driver> driversList = driverDAOService.findSimilar(tourPackage.getLocation(), startDate, endDate);
		requestDTO.setVehiclesList(vehiclesList);
		requestDTO.setDriversList(driversList);
		return requestDTO;
	}

	private String findVehicleType(long guestCount) {
		String vehicleType;
		if (guestCount <= 4) {
			vehicleType = SMALL_SIZE;
		} else if (4 < guestCount && guestCount <= 10) {
			vehicleType = MEDIUM_SIZE;
		} else {
			vehicleType = LARGE_SIZE;
		}
		return vehicleType;
	}

	@Override
	public String assignVehDriTour(VehicleDriverMapping vehicleDriverMapping) {
		String status = EMPTY_STRING;
		vehicleDriverMapping = vehDriMapDAOService.saveMapping(vehicleDriverMapping);
		if (vehicleDriverMapping.getVdmId() > 0) {
			TourPackageRequest tPR = tourPackageDAOService
					.getTourPackageRequestById((int) vehicleDriverMapping.getTourPackageRequestId());
			tPR.setVehicleDriverMappingId((int) vehicleDriverMapping.getVdmId());
			tourPackageDAOService.saveTourPackageRequest(tPR);
			User user = commonDAOService.findUserByUserId(tPR.getUserId());
			MailDTO mailDTO = new MailDTO();
			mailDTO.setUser(user);
			mailDTO.setTemplateName(TEMPLATE_TOUR_DETAILS);
			mailDTO.setVehicleDriverMapping(vehicleDriverMapping);
			mailDTO.setVehicle(vehicleDAOService.getVehicleByRegNum(vehicleDriverMapping.getVehicleRegNum()));
			mailDTO.setDriver(driverDAOService.getDriverByLicense(vehicleDriverMapping.getDriverLicense()));
			status = mailUtil.triggerMail(mailDTO);
		} else {
			status = EXCEPTION_OCCURED;
		}
		return status;
	}

	@Override
	public List<TourPackageRequest> getCustomerTours(Customer customer) {
		return tourPackageDAOService.getRequestListByUserName(customer.getUserName());
	}

	@Override
	public String cancelTourRequest(TourPackageRequest tourPackageRequest) {

		TourPackageRequest existingTPR = tourPackageDAOService
				.getTourPackageRequestById(tourPackageRequest.getTourPackageRequestId());
		if (existingTPR.getVehicleDriverMappingId() != 0) {
			existingTPR.setCancelled(YES);
			tourPackageDAOService.saveTourPackageRequest(existingTPR);
			return TOUR_CANCEL_SUCCESS;
		} else {
			return TOUR_CANCEL_FAILED;
		}

	}

	@Override
	public VehicleDriverMapping viewVDMDetails(TourPackageRequest tourPackageRequest) {
		return vehDriMapDAOService.getMappingByTourPackageRequestId(tourPackageRequest);
	}

}
