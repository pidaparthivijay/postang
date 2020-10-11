/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.model.Customer;
import com.postang.model.MailDTO;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.repo.RoomRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.UserRepository;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */

@Service
@Log4j2
public class RoomRequestServiceImpl implements RoomRequestService, Constants {

	MailUtil mailUtil = new MailUtil();

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	RoomRequestRepository roomReqRepo;

	@Autowired
	UserRepository userRepo;

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
	public Iterable<RoomRequest> getAllRoomRequests() {
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
	public Iterable<RoomRequest> getUnallocatedRoomRequests() {
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


}
