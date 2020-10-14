/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.dao.service.RewardPointsDAOService;
import com.postang.dao.service.RoomDAOService;
import com.postang.dao.service.RoomRequestDAOService;
import com.postang.domain.Customer;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.domain.User;
import com.postang.model.MailDTO;
import com.postang.service.RoomRequestService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */

@Service
@Log4j2
public class RoomRequestServiceImpl implements RoomRequestService, Constants {

	MailUtil mailUtil = new MailUtil();

	Util util = new Util();

	@Autowired
	RoomDAOService roomDAOService;

	@Autowired
	RoomRequestDAOService roomReqDAOService;

	@Autowired
	CommonDAOService commonDAOService;

	@Autowired
	RewardPointsDAOService rewardPointsDAOService;

	@Override
	public String cancelRoomRequest(int roomRequestId) {
		String cancellationStatus = "";
		RoomRequest roomRequest = roomReqDAOService.getRoomRequestByRequestId(roomRequestId);
		roomRequest.setRoomRequestStatus(CANCEL);
		RoomRequest roomReq = roomReqDAOService.saveRoomRequest(roomRequest);
		int userId = roomReq.getUserId();
		User user = commonDAOService.findUserByUserId(userId);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setUser(user);
		mailDTO.setRoomRequestId(roomRequestId);
		mailDTO.setTemplateName(
				roomReq.getRoomRequestStatus().equals(CANCEL) ? TEMPLATE_CANCEL_MAIL : TEMPLATE_CANCEL_FAIL_MAIL);
		cancellationStatus = mailUtil.triggerMail(mailDTO);
		return cancellationStatus;
	}


	@Override
	public List<RoomRequest> getAllRoomRequests() {
		return roomReqDAOService.getAllRoomRequests();
	}


	@Override
	public List<RoomRequest> getMyRequestsList(Customer customer) {
		return roomReqDAOService.getRequestListByUserId((int) customer.getUserId());
	}

	@Override
	public RoomRequest getRoomRequestByRequestId(int requestId) {
		return roomReqDAOService.getRoomRequestByRequestId(requestId);
	}

	@Override
	public List<RoomRequest> getUnallocatedRoomRequests() {
		return roomReqDAOService.getRoomRequestByStatus(PENDING);
	}

	@Override
	public User getUserById(int userId) {
		return commonDAOService.findUserByUserId(userId);
	}

	@Override
	public RoomRequest requestRoom(RoomRequest roomRequest) {
		roomRequest.setRoomRequestStatus(PENDING);
		return roomReqDAOService.saveRoomRequest(roomRequest);
	}

	@Override
	public RoomRequest saveRoomRequest(RoomRequest roomReq) {
		return roomReqDAOService.saveRoomRequest(roomReq);
	}


	@Override
	public String assignRoom(RoomRequest roomRequest) {
		log.info("assignRoom starts with: " + roomRequest);
		RoomRequest roomReq = this.getRoomRequestByRequestId(roomRequest.getRequestId());
		roomReq.setRoomNumber(roomRequest.getRoomNumber());
		roomReq.setRoomRequestStatus(ALLOCATED);
		roomReq = this.saveRoomRequest(roomReq);
		Room room = roomDAOService.getRoomByRoomNumber(roomRequest.getRoomNumber());
		room.setRoomRequestId(roomReq.getRequestId());
		room.setRoomStatus(OCCUPIED);
		room.setCheckInDate(roomRequest.getCheckInDate());
		room.setCheckOutDate(roomRequest.getCheckOutDate());
		roomDAOService.saveRoom(room);
		User user = this.getUserByUserName(roomReq.getUserName());
		rewardPointsDAOService.saveRewardPoints(util.allocateRewardPoints(user, ROOM_BOOKING));
		MailDTO mailDTO = new MailDTO();
		mailDTO.setEmailAddress(user.getUserMail());
		mailDTO.setTemplateName(TEMPLATE_ALLOCATION_MAIL);
		return mailUtil.triggerMail(mailDTO);
	}

	private User getUserByUserName(String userName) {
		return commonDAOService.findUserByUserName(userName);
	}

}
