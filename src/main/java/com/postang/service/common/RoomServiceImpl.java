/**
 * 
 */
package com.postang.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.repo.RoomRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.UserRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;
/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomRequestRepository roomReqRepo;
	
	@Autowired
	RoomRepository roomRepo;
	
	@Autowired
	UserRepository userRepo;

	Util util = new Util();

	@Override
	public RoomRequest getRoomRequestByRequestId(int requestId) {		
		return roomReqRepo.findByRequestId(requestId);
	}

	@Override
	public Room getRoomByRoomNumber(int roomNumber) {
		return roomRepo.findByRoomNumber(roomNumber);
	}

	@Override
	public RoomRequest saveRoomRequest(RoomRequest roomReq) {
		return roomReqRepo.save(roomReq);
		
	}

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
	}

	@Override
	public User getUserById(int userId) {
		return userRepo.findByUserId(userId);
	}

	
}
