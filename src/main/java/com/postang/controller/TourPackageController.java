package com.postang.controller;

import java.util.Date;
import java.util.List;

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
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.model.RequestDTO;
import com.postang.service.TourPackageService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class TourPackageController implements RequestMappings, Constants {

	@Autowired
	TourPackageService tourPackageService;

	MailUtil mailUtil = new MailUtil();

	Util util = new Util();

	/***************************
	 * Tour package Operations**
	 ***************************/

	@PostMapping(value = TOUR_PKG_CREATE)
	public RequestDTO createTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackage tourPackage = requestDTO.getTourPackage();
		try {
			tourPackage.setDeleted(NO);
			tourPackage.setTourPackageName(util.generateName(tourPackage));
			tourPackage = tourPackageService.saveTourPackage(tourPackage);
			requestDTO.setActionStatus(TOUR_PKG_CRT_SXS);
			requestDTO.setTourPackage(tourPackage);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_UPDATE)
	public RequestDTO updateTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackage tourPackage = requestDTO.getTourPackage();
		try {
			tourPackage = tourPackageService.saveTourPackage(tourPackage);
			tourPackage.setActionStatus(true);
			requestDTO.setTourPackage(tourPackage);
			return viewAllTourPackages(SUCCESS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@GetMapping(value = TOUR_PKG_VIEW_ALL)
	public RequestDTO viewAllTourPackages(String status) {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setTourPackageList(tourPackageService.viewAllTourPackages());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_DELETE_TOGGLE)
	public RequestDTO toggleDeleteTourPackage(@RequestBody RequestDTO requestDTO) {
		String tourPackageName = requestDTO.getTourPackage().getTourPackageName();
		try {
			TourPackage tourPackage = tourPackageService.findByName(tourPackageName);
			tourPackage.setDeleted(YES.equals(tourPackage.getDeleted()) ? NO : YES);
			TourPackage savedTourPackage = tourPackageService.saveTourPackage(tourPackage);
			requestDTO.setTourPackage(savedTourPackage);
			return viewAllTourPackages(YES.equals(savedTourPackage.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_BOOK)
	public RequestDTO bookTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackageRequest tourPackageRequest = requestDTO.getTourPackageRequest();
		try {
			tourPackageRequest.setRequestDate(new Date());
			tourPackageRequest.setBillStatus(BILL_PENDING);
			tourPackageRequest = tourPackageService.bookTourPackage(tourPackageRequest);
			requestDTO.setActionStatus(
					tourPackageRequest.getTourPackageRequestId() > 0 ? TOUR_PKG_BOOK_SXS : TOUR_PKG_BOOK_FAIL);
			requestDTO.setTourPackageRequest(tourPackageRequest);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@GetMapping(value = TOUR_BKNG_VIEW_ALL)
	public RequestDTO viewAllTourBookings() {
		RequestDTO requestDTO = new RequestDTO();
		try {
			List<TourPackageRequest> tourPackageRequestList = tourPackageService.getAllTourPackageBookings();
			requestDTO.setTourPackageRequestList(tourPackageRequestList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = FEASIBLE_VEHICLES_DRIVERS)
	public RequestDTO viewFeasibleVehiclesDrivers(@RequestBody RequestDTO requestDTO) {
		try {
			requestDTO = tourPackageService.viewFeasibleVehiclesDrivers(requestDTO.getTourPackageRequest());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_BKNG_ASSIGN)
	public RequestDTO assignVehDriTour(@RequestBody RequestDTO requestDTO) {
		try {
			requestDTO.setActionStatus(tourPackageService.assignVehDriTour(requestDTO.getVehicleDriverMapping()));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = CUSTOMER_GET_TOUR_BKNGS)
	public RequestDTO getMyTourBkngs(@RequestBody RequestDTO requestDTO) {
		try {
			requestDTO.setTourPackageRequestList(tourPackageService.getCustomerTours(requestDTO.getCustomer()));

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = CANCEL_TOUR_BKNG)
	public RequestDTO cancelTourBkng(@RequestBody RequestDTO requestDTO) {
		try {
			requestDTO.setActionStatus(tourPackageService.cancelTourRequest(requestDTO.getTourPackageRequest()));

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = VIEW_VD_MAPPING)
	public RequestDTO viewVDMDetails(@RequestBody RequestDTO requestDTO) {
		try {
			requestDTO.setVehicleDriverMapping(tourPackageService.viewVDMDetails(requestDTO.getTourPackageRequest()));

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}
}
