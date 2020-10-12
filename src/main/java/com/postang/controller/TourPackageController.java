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
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.model.RequestDTO;
import com.postang.service.TourPackageService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
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
		log.info("createTourPackage starts..." + tourPackage);
		try {
			tourPackage.setDeleted(NO);
			tourPackage.setTourPackageName(util.generateName(tourPackage));
			tourPackage = tourPackageService.saveTourPackage(tourPackage);
			requestDTO.setActionStatus(TOUR_PKG_CRT_SXS);
			requestDTO.setTourPackage(tourPackage);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = "/brw/updateTourPackage")
	public RequestDTO updateTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackage tourPackage = requestDTO.getTourPackage();
		log.info("updatePriceTourPackage starts..." + tourPackage);
		try {
			tourPackage = tourPackageService.saveTourPackage(tourPackage);
			tourPackage.setActionStatus(true);
			requestDTO.setTourPackage(tourPackage);
			return viewAllTourPackages(SUCCESS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updatePriceTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}

	@GetMapping(value = TOUR_PKG_VIEW_ALL)
	public RequestDTO viewAllTourPackages(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewAllTourPackages starts...");
		try {
			requestDTO.setTourPackageList(tourPackageService.viewAllTourPackages());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewAllTourPackages : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_DELETE_TOGGLE)
	public RequestDTO toggleDeleteTourPackage(@RequestBody RequestDTO requestDTO) {
		String tourPackageName = requestDTO.getTourPackage().getTourPackageName();
		log.info("toggleDeleteTourPackage starts..." + tourPackageName);
		try {
			TourPackage tourPackage = tourPackageService.findTourPackageByTourPackageName(tourPackageName);
			tourPackage.setDeleted(YES.equals(tourPackage.getDeleted()) ? NO : YES);
			TourPackage savedTourPackage = tourPackageService.saveTourPackage(tourPackage);
			requestDTO.setTourPackage(savedTourPackage);
			return viewAllTourPackages(YES.equals(savedTourPackage.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDeleteTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_BOOK)
	public RequestDTO bookTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackageRequest tourPackageRequest = requestDTO.getTourPackageRequest();
		log.info("bookTourPackage starts..." + tourPackageRequest.getUserId());
		try {
			tourPackageRequest.setRequestDate(new Date());
			tourPackageRequest.setBillStatus(BILL_PENDING);
			tourPackageRequest = tourPackageService.bookTourPackage(tourPackageRequest);
			requestDTO.setActionStatus(
					tourPackageRequest.getTourPackageRequestId() > 0 ? TOUR_PKG_BOOK_SXS : TOUR_PKG_BOOK_FAIL);
			requestDTO.setTourPackageRequest(tourPackageRequest);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in bookTourPackage: " + ex);
		}
		return requestDTO;
	}

}
