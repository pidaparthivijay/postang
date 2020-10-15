/**
 * 
 */
package com.postang.service.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.AmenityDAOService;
import com.postang.dao.service.CommonDAOService;
import com.postang.dao.service.RoomDAOService;
import com.postang.dao.service.RoomRequestDAOService;
import com.postang.dao.service.TourPackageDAOService;
import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.domain.User;
import com.postang.model.MailDTO;
import com.postang.model.PendingBillRequest;
import com.postang.service.BillingService;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@Service
public class BillingServiceImpl implements BillingService, Constants {

	MailUtil mailUtil = new MailUtil();

	PDFUtil pdfUtil = new PDFUtil();

	@Autowired
	RoomDAOService roomDAOService;

	@Autowired
	AmenityDAOService amenityDAOService;

	@Autowired
	RoomRequestDAOService roomReqDAOService;

	@Autowired
	TourPackageDAOService tourPackageDAOService;

	@Autowired
	CommonDAOService commonDAOService;

	Util util = new Util();

	@Override
	public double generateBill(String custEmail) {
		User user = commonDAOService.findUserByUserMail(custEmail);
		List<RoomRequest> roomRequestList = roomReqDAOService.getRequestListByUserId((int) user.getUserId());
		List<RoomRequest> billPendingList = roomRequestList.stream().filter(p -> BILL_PENDING.equals(p.getBillStatus()))
				.collect(Collectors.toList());
		List<Room> totalRoomsList = new ArrayList<>();
		for (RoomRequest roomRequest : billPendingList) {
			List<Room> roomsList = roomDAOService.findByRoomRequestId(roomRequest.getRequestId());
			totalRoomsList.addAll(roomsList);
		}
		return util.generateBillForRooms(totalRoomsList);

	}

	@Override
	public ByteArrayInputStream generatedBillPdf(String custEmail) {
		User user = commonDAOService.findUserByUserMail(custEmail);
		if (user != null) {
			return pdfUtil.generatePdf(this.getPendingBillRequests(custEmail),
					commonDAOService.findUserByUserMail(custEmail).getName());
		} else {
			return null;
		}
	}

	@Override
	public List<PendingBillRequest> getPendingBillRequests(String custEmail) {
		List<PendingBillRequest> pendingBillRequests = new ArrayList<>();
		User user = commonDAOService.findUserByUserMail(custEmail);

		if (user != null) {
			String userName = user.getUserName();
			List<RoomRequest> roomRequestList = roomReqDAOService.getRequestListByUserName(userName);
			List<AmenityRequest> amenityRequestList = amenityDAOService.getRequestListByUserName(userName);
			List<TourPackageRequest> tourPackageRequestList = tourPackageDAOService
					.getRequestListByUserName(user.getUserName());
			List<RoomRequest> billPendingRooms = roomRequestList.stream()
					.filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());
			List<AmenityRequest> billPendingAmenities = amenityRequestList.stream()
					.filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());
			List<TourPackageRequest> billPendingTours = tourPackageRequestList.stream()
					.filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(billPendingRooms)) {
				pendingBillRequests.addAll(this.convertToBillPendingRequest(billPendingRooms));
			}
			if (!CollectionUtils.isEmpty(amenityRequestList)) {
				pendingBillRequests.addAll(this.convertAmenityReqToBillPendingRequest(billPendingAmenities));
			}
			if (!CollectionUtils.isEmpty(amenityRequestList)) {
				pendingBillRequests.addAll(this.convertTourReqToBillPendingRequest(billPendingTours));
			}
			PendingBillRequest taxes = new PendingBillRequest();
			if (!CollectionUtils.isEmpty(pendingBillRequests)) {
				DoubleSummaryStatistics beforeTaxes = pendingBillRequests.stream()
						.collect(Collectors.summarizingDouble(PendingBillRequest::getBillAmount));
				double tax = beforeTaxes.getSum() + (beforeTaxes.getSum() * 0.15);
				taxes.setBillAmount(tax);
				taxes.setTypeOfRequest(GST_OTHERS);
				pendingBillRequests.add(taxes);
			}
			PendingBillRequest totalBillPendingRequest = new PendingBillRequest();
			if (!CollectionUtils.isEmpty(pendingBillRequests)) {

				DoubleSummaryStatistics stats = pendingBillRequests.stream()
						.collect(Collectors.summarizingDouble(PendingBillRequest::getBillAmount));

				totalBillPendingRequest.setBillAmount(stats.getSum());
				totalBillPendingRequest.setTypeOfRequest(TOTAL_BILL_AMOUNT);
			} else {
				totalBillPendingRequest.setBillCode(NO_PENDING_BILLS);
			}
			pendingBillRequests.add(totalBillPendingRequest);
			return pendingBillRequests;
		}
		return new ArrayList<>();

	}

	private List<PendingBillRequest> convertToBillPendingRequest(List<RoomRequest> billPendingList) {
		List<PendingBillRequest> billPendingRequestList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(billPendingList)) {
			for (RoomRequest roomRequest : billPendingList) {
				PendingBillRequest pendingBillRequest = new PendingBillRequest();
				pendingBillRequest.setBillCode(roomRequest.getGuestName() + UNDERSCORE + roomRequest.getRoomModel()
						+ UNDERSCORE + roomRequest.getRoomCategory() + UNDERSCORE + roomRequest.getRoomType());
				pendingBillRequest.setRequestId(roomRequest.getRequestId());
				List<Room> roomsList = roomDAOService.findByRoomRequestId(roomRequest.getRequestId());
				pendingBillRequest.setBillAmount(util.generateBillForRooms(roomsList));
				pendingBillRequest.setTypeOfRequest(ROOM_REQUEST);
				pendingBillRequest.setRequestDate(roomRequest.getRequestDate());
				if (!CollectionUtils.isEmpty(roomsList)) {
					billPendingRequestList.add(pendingBillRequest);
				}
			}
		} else {
			return new ArrayList<>();
		}
		return billPendingRequestList;
	}

	private List<PendingBillRequest> convertTourReqToBillPendingRequest(
			List<TourPackageRequest> tourPackageRequestList) {
		List<PendingBillRequest> billPendingRequestList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(tourPackageRequestList)) {
			for (TourPackageRequest tourPackageRequest : tourPackageRequestList) {
				PendingBillRequest pendingBillRequest = new PendingBillRequest();
				pendingBillRequest.setBillCode(tourPackageRequest.getUserName() + UNDERSCORE
						+ tourPackageRequest.getTourPackageName() + UNDERSCORE + tourPackageRequest.getGuestCount());
				TourPackage tourPackage = tourPackageDAOService
						.findTourPackageByTourPackageName(tourPackageRequest.getTourPackageName());
				pendingBillRequest.setRequestId(tourPackageRequest.getTourPackageRequestId());
				pendingBillRequest
						.setBillAmount(util.generateBillForTours(tourPackage, tourPackageRequest.getGuestCount()));
				pendingBillRequest.setTypeOfRequest(TOUR_REQUEST);
				pendingBillRequest.setRequestDate(tourPackageRequest.getRequestDate());
				billPendingRequestList.add(pendingBillRequest);
			}
		} else {
			return new ArrayList<>();
		}
		return billPendingRequestList;
	}

	private List<PendingBillRequest> convertAmenityReqToBillPendingRequest(List<AmenityRequest> amenityRequestList) {
		List<PendingBillRequest> billPendingRequestList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(amenityRequestList)) {
			for (AmenityRequest amenityRequest : amenityRequestList) {
				PendingBillRequest pendingBillRequest = new PendingBillRequest();
				pendingBillRequest.setBillCode(amenityRequest.getUserName() + UNDERSCORE + amenityRequest.getAmenityId()
						+ UNDERSCORE + amenityRequest.getNoOfDays());
				pendingBillRequest.setRequestId(amenityRequest.getAmenityRequestId());
				Amenity amenity = amenityDAOService.findByAmenityId(amenityRequest.getAmenityId());
				pendingBillRequest.setBillAmount(util.generateBillForAmenities(amenity, amenityRequest.getNoOfDays()));
				pendingBillRequest.setTypeOfRequest(AMENITY_REQUEST);
				pendingBillRequest.setRequestDate(amenityRequest.getRequestDate());
				billPendingRequestList.add(pendingBillRequest);

			}
		} else {
			return new ArrayList<>();
		}
		return billPendingRequestList;
	}

	@Override
	public String triggerMailBill(String custEmail) {
		User user = commonDAOService.findUserByUserMail(custEmail);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setPendingBillRequests(this.getPendingBillRequests(custEmail));
		mailDTO.setUser(user);
		mailDTO.setTemplateName(TEMPLATE_BILL_MAIL_CUST);
		return mailUtil.triggerMail(mailDTO);
	}
}
