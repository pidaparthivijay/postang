package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Constants;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.service.common.CommonService;
import com.postang.service.common.RoomService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class RoomAllocationController {

	@Autowired
	CommonService commonService;

	@Autowired
	RoomService roomService;

	MailUtil mailUtil = new MailUtil();

	@PostMapping(value = "/brw/assignRoomToRequest")
	public String assignRoomToRequest(@RequestBody RoomRequest roomRequest) {
		String customerJsonString = "";
		log.info("assignRoomToRequest starts..." + roomRequest);
		try {
			RoomRequest roomReq = roomService.getRoomRequestByRequestId(roomRequest.getRequestId());
			roomReq.setRoomNumber(roomRequest.getRoomNumber());
			roomReq.setRoomRequestStatus(Constants.ALLOCATED);
			roomReq = roomService.saveRoomRequest(roomReq);
			Room room = roomService.getRoomByRoomNumber(roomRequest.getRoomNumber());
			room.setRoomRequestId(roomReq.getRequestId());
			room.setRoomStatus(Constants.OCCUPIED);
			room.setCheckInDate(roomRequest.getCheckInDate());
			room.setCheckOutDate(roomRequest.getCheckOutDate());
			roomService.saveRoom(room);
			User user = roomService.getUserById(roomReq.getUserId());
			commonService.allocateRewardPoints(user, Constants.ROOM_BOOKING);
			String mailStatus = mailUtil.sendAllocationMail(user.getUserMail());
			customerJsonString = new ObjectMapper().writeValueAsString(mailStatus);
			log.info("assignRoomToRequest ends...");
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
			ex.printStackTrace();
		}
		return customerJsonString;
	}

	
}
