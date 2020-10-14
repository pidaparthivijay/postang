/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.dao.service.RoomRequestDAOService;
import com.postang.domain.RoomRequest;
import com.postang.repo.RoomRequestRepository;

/**
 * @author Subrahmanya Vijay
 *
 */

@Service
@Repository
public class RoomRequestDAOServiceImpl implements RoomRequestDAOService, Constants {

	@Autowired
	RoomRequestRepository roomReqRepo;

	@Override
	public List<RoomRequest> getAllRoomRequests() {
		return roomReqRepo.findAll();
	}


	@Override
	public List<RoomRequest> getRequestListByUserId(int userId) {
		return roomReqRepo.findByUserId(userId);
	}

	@Override
	public List<RoomRequest> getRoomRequestByStatus(String status) {
		return roomReqRepo.findByRoomRequestStatus(status);
	}

	@Override
	public RoomRequest getRoomRequestByRequestId(int requestId) {
		return roomReqRepo.findByRequestId(requestId);
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
	public List<RoomRequest> getRequestListByUserName(String userName) {
		return roomReqRepo.findByUserName(userName);
	}

}
