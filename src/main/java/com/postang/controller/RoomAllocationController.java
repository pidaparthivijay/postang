package com.postang.controller;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postang.model.Constants;
import com.postang.model.RequestDTO;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.service.common.AdminService;
import com.postang.service.common.CommonService;
import com.postang.service.common.RoomService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@RestController
public class RoomAllocationController implements Constants{

	@Autowired
	CommonService commonService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	RoomService roomService;

	MailUtil mailUtil = new MailUtil();

	/***************************
	 * Room Request Operations**
	 ***************************/	

	@GetMapping(value = "/brw/getAllRoomRequests")
	public RequestDTO getAllRoomRequests() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getAllRoomRequests starts...");
		try {
			Iterable<RoomRequest> roomReqList = adminService.getUnallocatedRoomRequests();
			requestDTO.setRoomRequestList(
					StreamSupport.stream(roomReqList.spliterator(), false).collect(Collectors.toList()));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getAllRoomRequests : " + ex.getMessage());
		}
		return requestDTO;
	}
	
	@PostMapping(value = "/brw/assignRoomToRequest")
	public RequestDTO assignRoomToRequest(@RequestBody RequestDTO requestDTO) {
		RoomRequest roomRequest=requestDTO.getRoomRequest();
		log.info("assignRoomToRequest starts..." + roomRequest);
		try {
			RoomRequest roomReq = roomService.getRoomRequestByRequestId(roomRequest.getRequestId());
			roomReq.setRoomNumber(roomRequest.getRoomNumber());
			roomReq.setRoomRequestStatus(ALLOCATED);
			roomReq = roomService.saveRoomRequest(roomReq);
			Room room = roomService.getRoomByRoomNumber(roomRequest.getRoomNumber());
			room.setRoomRequestId(roomReq.getRequestId());
			room.setRoomStatus(OCCUPIED);
			room.setCheckInDate(roomRequest.getCheckInDate());
			room.setCheckOutDate(roomRequest.getCheckOutDate());
			roomService.saveRoom(room);
			User user = roomService.getUserById(roomReq.getUserId());
			commonService.allocateRewardPoints(user, ROOM_BOOKING);
			String mailStatus = mailUtil.sendAllocationMail(user.getUserMail());
			requestDTO.setActionStatus(mailStatus);
			log.info("assignRoomToRequest ends...");
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.info("Exception is: " + ex);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	
}
