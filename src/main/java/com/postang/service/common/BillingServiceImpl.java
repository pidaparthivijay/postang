/**
 * 
 */
package com.postang.service.common;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.model.MailDTO;
import com.postang.model.PendingBillRequest;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.repo.RoomRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.UserRepository;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class BillingServiceImpl implements BillingService, Constants {


	MailUtil mailUtil = new MailUtil();

	PDFUtil pdfUtil = new PDFUtil();

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	RoomRequestRepository roomReqRepo;

	@Autowired
	UserRepository userRepo;

	Util util = new Util();

	public List<PendingBillRequest> convertToBillPendingRequest(List<RoomRequest> billPendingList) {
		List<PendingBillRequest> billPendingRequestList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(billPendingList)) {
			for (RoomRequest roomRequest : billPendingList) {
				PendingBillRequest pendingBillRequest = new PendingBillRequest();
				pendingBillRequest.setBillCode(roomRequest.getGuestName() + UNDERSCORE + roomRequest.getRoomModel()
						+ UNDERSCORE + roomRequest.getRoomCategory() + UNDERSCORE + roomRequest.getRoomType());
				pendingBillRequest.setRequestId(roomRequest.getRequestId());
				List<Room> roomsList = roomRepo.findByRoomRequestId(roomRequest.getRequestId());
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

	@Override
	public double generateBill(String custEmail) {
		User user = userRepo.findByUserMail(custEmail);
		List<RoomRequest> roomRequestList = roomReqRepo.findByUserId((int) user.getUserId());
		List<RoomRequest> billPendingList = roomRequestList.stream().filter(p -> BILL_PENDING.equals(p.getBillStatus()))
				.collect(Collectors.toList());
		List<Room> totalRoomsList = new ArrayList<>();
		for (RoomRequest roomRequest : billPendingList) {
			List<Room> roomsList = roomRepo.findByRoomRequestId(roomRequest.getRequestId());
			totalRoomsList.addAll(roomsList);
		}
		return util.generateBillForRooms(totalRoomsList);

	}

	@Override
	public ByteArrayInputStream generatedBillPdf(String custEmail) {
		User user = userRepo.findByUserMail(custEmail);
		if (user != null) {
			return pdfUtil.generatePdf(this.getPendingBillRequests(custEmail),
					userRepo.findByUserMail(custEmail).getName());
		} else {
			return null;
		}
	}

	@Override
	public List<PendingBillRequest> getPendingBillRequests(String custEmail) {
		List<PendingBillRequest> pendingBillRequests = new ArrayList<>();
		User user = userRepo.findByUserMail(custEmail);
		if (user != null) {
			List<RoomRequest> roomRequestList = roomReqRepo.findByUserId((int) user.getUserId());
			List<RoomRequest> billPendingList = roomRequestList.stream()
					.filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());
			PendingBillRequest totalBillPendingRequest = new PendingBillRequest();
			if (!CollectionUtils.isEmpty(billPendingList)) {
				pendingBillRequests.addAll(this.convertToBillPendingRequest(billPendingList));
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

	@Override
	public String triggerMailBill(String custEmail) {
		User user = userRepo.findByUserMail(custEmail);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setPendingBillRequests(this.getPendingBillRequests(custEmail));
		mailDTO.setUser(user);
		mailDTO.setTemplateName(TEMPLATE_BILL_MAIL_CUST);
		return mailUtil.triggerMail(mailDTO);
	}
}
