/**
 * 
 */
package com.postang.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.postang.model.AmenityRequest;
import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.model.PendingBillRequest;
import com.postang.model.RequestDTO;
import com.postang.model.RewardPoints;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackageRequest;
import com.postang.service.common.CommonService;
import com.postang.service.common.CustomerService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@Log4j2
@CrossOrigin
public class CustomerController implements Constants{

	@Autowired
	CustomerService customerService;

	MailUtil mailUtil = new MailUtil();
	
	@Autowired
	CommonService commonService;
	
	/*************************
	 ***Customer Operations***
	 *************************/
	
	@PostMapping(value = "/brw/registerCustomer")
	public RequestDTO registerCustomer(@RequestBody RequestDTO requestDTO) {
		Customer customer=requestDTO.getCustomer();
		log.info("registerCustomer starts..." + customer);
		try {
			customer = customerService.saveCustomer(customer);

			if (customer.getCustId() > 0) {
				customer.setActionStatus(true);
				customer.setStatusMessage("");
				String mailStatus = mailUtil.sendSignUpMail(customer);
				log.info(mailStatus);
			}
			requestDTO.setCustomer(customer);
			requestDTO.setActionStatus(customer.getCustId()>0?
					CUST_REG_SXS:CUST_REG_FAIL);
			log.info("registerCustomer ends..." + customer.getCustId());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.info("Exception occurred in registerCustomer: " + ex);
		}
		return requestDTO;
	}
	
	@RequestMapping(value = "/brw/getCustomerDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getCustomerDetails(@RequestBody RequestDTO requestDTO) {
		 Customer customer=requestDTO.getCustomer();
		 log.info("getCustomerDetails starts...");
		try {
			customer = customerService.getCustomerDetails(customer);
			log.info("Infor is: " + customerService.getCustomerDetails(customer).toString());
			requestDTO.setCustomer(customer);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in getCustomerDetails: " + ex);
		}
		return requestDTO;
	}

	/*********************
	 ***Bill Operations***
	 *********************/
	
	@PostMapping(value = "/brw/getPendingBillRequests")
	public RequestDTO getPendingBillRequests(@RequestBody RequestDTO requestDTO) {
		String custEmail=requestDTO.getCustomer().getCustEmail();
		log.info("getPendingBillRequests starts..." + custEmail);
		try {
			List<PendingBillRequest> pendingBillRequests= customerService.getPendingBillRequests(custEmail);
			PendingBillRequest totalBillPendingRequest= new PendingBillRequest(); 
			double customerBill=customerService.generateBill(custEmail);
			totalBillPendingRequest.setBillAmount(customerBill);
			totalBillPendingRequest.setTypeOfRequest(TOTAL_BILL_AMOUNT);
			pendingBillRequests.add(totalBillPendingRequest);
			requestDTO.setPendingBillRequests(pendingBillRequests);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);  
			log.error("Exception occured in getPendingBillRequests: " + e);
			e.printStackTrace();

		}
		return requestDTO;
	}

	
	/*****************************
	 ***Room Request Operations***
	 *****************************/
	
	@RequestMapping(value = "/brw/requestRoom", method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO requestRoom(@RequestBody RequestDTO requestDTO) {		
		 RoomRequest roomRequest = requestDTO.getRoomRequest();
		log.info("requestRoom starts..." + roomRequest.getUserId());
		try {
			roomRequest= customerService.requestRoom(roomRequest);
			requestDTO.setActionStatus(roomRequest.getRequestId()>0?
					ROOM_BOOK_SXS:ROOM_BOOK_FAIL);	
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in requestRoom: " + ex);
		}
		return requestDTO;
	}

	@RequestMapping(value = "/brw/cancelRequest", method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO cancelRequest(@RequestBody RequestDTO requestDTO) {
		int roomRequestId=requestDTO.getRoomRequestId();
		log.info("cancelRequest starts...");
		try {
			requestDTO.setActionStatus(customerService.cancelRoomRequest(roomRequestId));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in cancelRequest : " + ex);
			ex.printStackTrace();
		}
		return requestDTO;
	}
	
	@RequestMapping(value = "/brw/getMyRequestsList", method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getMyRequestsList(@RequestBody RequestDTO requestDTO) {
		Customer customer=requestDTO.getCustomer();
		List<RoomRequest> roomReqDetails = null;
		log.info("getMyRequestsList starts...");
		try {
			roomReqDetails = customerService.getMyRequestsList(customer);
			requestDTO.setRoomRequestList(roomReqDetails);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in getMyRequestsList : " + ex);
			ex.printStackTrace();
		}
		return requestDTO;
	}
		

	/*****************************
	 ***Tour Package Operations***
	 *****************************/

	@PostMapping(value = "/brw/bookTourPackage")
	public RequestDTO bookTourPackage(@RequestBody RequestDTO requestDTO){
		TourPackageRequest tourPackageRequest=requestDTO.getTourPackageRequest();		
		log.info("bookTourPackage starts..." + tourPackageRequest.getUserId());
		try {
			tourPackageRequest.setRequestDate(new Date());
			tourPackageRequest.setBillStatus(BILL_PENDING);
			tourPackageRequest= customerService.bookTourPackage(tourPackageRequest);
			requestDTO.setActionStatus(tourPackageRequest.getTourPackageRequestId()>0?
					TOUR_PKG_BOOK_SXS:TOUR_PKG_BOOK_FAIL);				
			requestDTO.setTourPackageRequest(tourPackageRequest);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in bookTourPackage: " + ex);
		}
		return requestDTO;
	}

	
	/******************************
	 ***Reward Points Operations***
	 ******************************/
	
	@PostMapping(value = "/brw/viewRewardPoints")
	public RequestDTO viewRewardPoints(@RequestBody RequestDTO requestDTO) {
		long userId=requestDTO.getUserId();
		log.info("viewRewardPoints starts..." + userId);
		try {
			List<RewardPoints> rewardPointsList = commonService.getRewardPointsByUserId(userId);
			if (CollectionUtils.isEmpty(rewardPointsList)) {
				log.info("viewRewardPoints returns with" + NO_REWARDS_FOUND);
				requestDTO.setActionStatus(NO_REWARDS_FOUND);
				return requestDTO;
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
				requestDTO.setRewardPointsList(StreamSupport.stream(rewardPointsList.spliterator(), false).collect(Collectors.toList()));
				log.info("viewRewardPoints ends with" + totalCount);
			}

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.info("Exception is: " + ex);
			ex.printStackTrace();
		}

		return requestDTO;
	}

	
	/************************
	 ***Amenity Operations***
	 ************************/
	
	@PostMapping(value = "/brw/requestAmenity")
	public RequestDTO requestAmenity(@RequestBody RequestDTO requestDTO) {
		AmenityRequest amenityRequest=requestDTO.getAmenityRequest();
		log.info("requestAmenity starts..." + amenityRequest);
		try {
			amenityRequest = customerService.requestAmenity(amenityRequest);
			requestDTO.setActionStatus(
					(amenityRequest != null && amenityRequest.getAmenityRequestId() > 0) ? AMNT_RQST_SXS
							: AMNT_RQST_FAIL);
			requestDTO.setAmenityRequest(amenityRequest);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in requestAmenity: " + ex.getMessage());
		}
		return requestDTO;
	}

}
