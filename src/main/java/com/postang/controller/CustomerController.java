/**
 * 
 */
package com.postang.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.model.RewardPoints;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackageRequest;
import com.postang.service.common.CommonService;
import com.postang.service.common.CustomerService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@RestController
@Log4j2
public class CustomerController implements Constants{

	@Autowired
	CustomerService customerService;

	@Autowired
	CommonService commonService;
	
	@RequestMapping(value = "/brw/getCustomerDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String getCustomerDetails(@RequestBody Customer customer) {
		String customerJsonString = "";
		Customer custDetails = null;
		log.info("getCustomerDetails starts...");
		try {
			custDetails = customerService.getCustomerDetails(customer);
			log.info("Infor is: " + customerService.getCustomerDetails(customer).toString());
			if (custDetails != null)
				customerJsonString = new ObjectMapper().writeValueAsString(custDetails);
		} catch (Exception ex) {
			log.error("Exception occured in getCustomerDetails: " + ex);
		}
		return customerJsonString;
	}

	@RequestMapping(value = "/brw/getMyRequestsList", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMyRequestsList(@RequestBody Customer customer) {
		String roomRequestJson = "";
		List<RoomRequest> roomReqDetails = null;
		log.info("getMyRequestsList starts...");
		try {
			roomReqDetails = customerService.getMyRequestsList(customer);
			if (roomReqDetails != null)
				roomRequestJson = new ObjectMapper().writeValueAsString(roomReqDetails);
		} catch (Exception ex) {
			log.error("Exception occured in getMyRequestsList : " + ex);
			ex.printStackTrace();
		}
		return roomRequestJson;
	}

	@RequestMapping(value = "/brw/cancelRequest", method = { RequestMethod.GET, RequestMethod.POST })
	public String cancelRequest(@RequestBody int roomRequestId) {
		log.info("cancelRequest starts...");
		try {
			return customerService.cancelRoomRequest(roomRequestId);

		} catch (Exception ex) {
			log.error("Exception occured in cancelRequest : " + ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/brw/requestRoom", method = { RequestMethod.GET, RequestMethod.POST })
	public String requestRoom(@RequestBody RoomRequest roomRequest) {
		String roomRequestJson = "";
		RoomRequest roomReqDetails = null;
		log.info("requestRoom starts..." + roomRequest.getUserId());
		try {
			roomReqDetails = customerService.requestRoom(roomRequest);
			if (roomReqDetails != null)
				roomRequestJson = new ObjectMapper().writeValueAsString(roomReqDetails);
		} catch (Exception ex) {
			log.error("Exception occured in requestRoom: " + ex);
		}
		return roomRequestJson;
	}
	
	@PostMapping(value = "/brw/bookTourPackage")
	public String bookTourPackage(@RequestBody TourPackageRequest tourPackageRequest) {
		String tourPackageReqJson = "";
		TourPackageRequest tourPackageReqDetails = null;
		log.info("bookTourPackage starts..." + tourPackageRequest.getUserId());
		try {
			tourPackageRequest.setRequestDate(new Date());
			tourPackageRequest.setBillStatus(BILL_PENDING);
			tourPackageReqDetails = customerService.bookTourPackage(tourPackageRequest);
			if (tourPackageReqDetails != null)
				tourPackageReqJson = new ObjectMapper().writeValueAsString(tourPackageReqDetails);
		} catch (Exception ex) {
			log.error("Exception occured in bookTourPackage: " + ex);
		}
		return tourPackageReqJson;
	}
	
	@PostMapping(value = "/brw/viewRewardPoints")
	public String viewRewardPoints(@RequestBody long userId) {
		String rewardsJsonString = "";
		log.info("viewRewardPoints starts..." + userId);
		try {
			List<RewardPoints> rewardPointsList = commonService.getRewardPointsByUserId(userId);
			if (CollectionUtils.isEmpty(rewardPointsList)) {
				log.info("viewRewardPoints returns with" + NO_REWARDS_FOUND);
				return NO_REWARDS_FOUND;
			} else {
				long totalCount = 0l;
				for (RewardPoints rewardPoints : rewardPointsList) {
					if (!EXPIRED.equals(rewardPoints.getPointsStatus())) {
						totalCount += rewardPoints.getPointsEarned();
					}
				}
				RewardPoints rewardPoints = new RewardPoints();
				BeanUtils.copyProperties(rewardPointsList.get(0), rewardPoints);
				rewardPoints.setPointsTransactionName(TOTAL_POINTS);
				rewardPoints.setPointsEarned(totalCount);
				rewardPoints.setPointsStatus(null);
				rewardPoints.setPointsExpiryDate(null);
				rewardPoints.setPointsEarnedDate(null);
				rewardPointsList.add(rewardPoints);
				rewardsJsonString = new ObjectMapper().writeValueAsString(rewardPointsList);
				log.info("viewRewardPoints ends with" + totalCount);
			}

		} catch (Exception ex) {
			log.info("Exception is: " + ex);
			ex.printStackTrace();
		}

		return rewardsJsonString;
	}


}
