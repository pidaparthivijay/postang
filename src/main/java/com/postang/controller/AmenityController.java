package com.postang.controller;

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
import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
import com.postang.model.RequestDTO;
import com.postang.service.AmenityService;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class AmenityController implements RequestMappings, Constants {

	@Autowired
	AmenityService amenityService;

	/**********************
	 * Amenity Operations**
	 **********************/

	@PostMapping(value = AMENITY_CREATE)
	public RequestDTO createAmenity(@RequestBody RequestDTO requestDTO) {
		Amenity amenity = requestDTO.getAmenity();
		try {
			amenity.setDeleted(NO);
			amenity = amenityService.saveAmenity(amenity);
			requestDTO.setActionStatus((amenity != null && amenity.getAmenityId() > 0) ? AMNT_CRT_SXS : AMNT_CRT_FAIL);
			return viewAllAmenities(requestDTO.getActionStatus());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = AMENITY_UPDATE)
	public RequestDTO updateAmenity(@RequestBody RequestDTO requestDTO) {
		Amenity amenity = requestDTO.getAmenity();
		try {
			amenity = amenityService.saveAmenity(amenity);
			requestDTO.setAmenity(amenity);
			return viewAllAmenities(
					(amenity != null && amenity.getAmenityId() > 0) ? AMNT_UPDATE_SXS : AMNT_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = AMENITY_DELETE_TOGGLE)
	public RequestDTO toggleDeleteAmenity(@RequestBody RequestDTO requestDTO) {
		String amenityName = requestDTO.getAmenity().getAmenityName();
		try {
			Amenity amenity = amenityService.findAmenityByAmenityName(amenityName);
			amenity.setDeleted(YES.equals(amenity.getDeleted()) ? NO : YES);
			Amenity savedAmenity = amenityService.saveAmenity(amenity);
			requestDTO.setAmenity(savedAmenity);
			return viewAllAmenities(YES.equals(savedAmenity.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@GetMapping(value = AMENITY_VIEW_ALL)
	public RequestDTO viewAllAmenities(String status) {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setAmenityList(amenityService.viewAllAmenities());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = CUSTOMER_AMENITY_REQUEST)
	public RequestDTO requestAmenity(@RequestBody RequestDTO requestDTO) {
		AmenityRequest amenityRequest = requestDTO.getAmenityRequest();
		try {
			amenityRequest = amenityService.requestAmenity(amenityRequest);
			requestDTO.setActionStatus(
					(amenityRequest != null && amenityRequest.getAmenityRequestId() > 0) ? AMNT_RQST_SXS
							: AMNT_RQST_FAIL);
			requestDTO.setAmenityRequest(amenityRequest);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}
}
