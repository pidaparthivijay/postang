/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.RewardPointsDAOService;
import com.postang.dao.service.RoomDAOService;
import com.postang.domain.Customer;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.domain.User;
import com.postang.model.MailDTO;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.UserRepository;
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
	RoomRequestRepository roomReqRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RewardPointsDAOService rewardPointsDAOService;

	@Override
	public String cancelRoomRequest(int roomRequestId) {
		String cancellationStatus = "";
		RoomRequest roomRequest = roomReqRepo.findByRequestId(roomRequestId);
		roomRequest.setRoomRequestStatus(CANCEL);
		RoomRequest roomReq = roomReqRepo.save(roomRequest);
		int userId = roomReq.getUserId();
		User user = userRepo.findByUserId(userId);
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
		return roomReqRepo.findAll();
	}


	@Override
	public List<RoomRequest> getMyRequestsList(Customer customer) {
		return roomReqRepo.findByUserId((int) customer.getUserId());
	}

	@Override
	public RoomRequest getRoomRequestById(int roomRequestId) {
		return roomReqRepo.findByRequestId(roomRequestId);
	}

	@Override
	public RoomRequest getRoomRequestByRequestId(int requestId) {
		return roomReqRepo.findByRequestId(requestId);
	}

	@Override
	public List<RoomRequest> getUnallocatedRoomRequests() {
		return roomReqRepo.findByRoomRequestStatus(PENDING);
	}

	@Override
	public User getUserById(int userId) {
		return userRepo.findByUserId(userId);
	}

	@Override
	public RoomRequest requestRoom(RoomRequest roomRequest) {
		roomRequest.setRoomRequestStatus(PENDING);
		return roomReqRepo.save(roomRequest);
	}

	@Override
	public RoomRequest saveRoomRequest(RoomRequest roomReq) {
		return roomReqRepo.save(roomReq);
	}


	@Override
	public String assignRoom(RoomRequest roomRequest) {
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
		User user = this.getUserById(roomReq.getUserId());
		rewardPointsDAOService.saveRewardPoints(util.allocateRewardPoints(user, ROOM_BOOKING));
		MailDTO mailDTO = new MailDTO();
		mailDTO.setEmailAddress(user.getUserMail());
		mailDTO.setTemplateName(TEMPLATE_ALLOCATION_MAIL);
		return mailUtil.triggerMail(mailDTO);
	}

}
