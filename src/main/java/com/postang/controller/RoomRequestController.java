/**
 * 
 */
package com.postang.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.model.Customer;
import com.postang.model.MailDTO;
import com.postang.model.RequestDTO;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.service.common.RewardPointsService;
import com.postang.service.common.RoomService;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@Log4j2
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class RoomRequestController implements RequestMappings, Constants {

	@Autowired
	RoomService roomService;

	MailUtil mailUtil = new MailUtil();
	Util util = new Util();

	PDFUtil pdfUtil = new PDFUtil();

	@Autowired
	RewardPointsService rewardPointsService;

	/*****************************
	 *** Room Request Operations***
	 *****************************/

	@RequestMapping(value = ROOM_REQUEST_CREATE, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO requestRoom(@RequestBody RequestDTO requestDTO) {
		RoomRequest roomRequest = requestDTO.getRoomRequest();
		log.info("requestRoom starts..." + roomRequest.getUserId());
		try {
			roomRequest = roomService.requestRoom(roomRequest);
			requestDTO.setActionStatus(roomRequest.getRequestId() > 0 ? ROOM_BOOK_SXS : ROOM_BOOK_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in requestRoom: " + ex);
		}
		return requestDTO;
	}

	@RequestMapping(value = CUSTOMER_CANCEL_ROOM, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO cancelRequest(@RequestBody RequestDTO requestDTO) {
		int roomRequestId = requestDTO.getRoomRequestId();
		log.info("cancelRequest starts...");
		try {
			requestDTO.setActionStatus(roomService.cancelRoomRequest(roomRequestId));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in cancelRequest : " + ex);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@RequestMapping(value = CUSTOMER_GET_ALL_REQUESTS, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getMyRequestsList(@RequestBody RequestDTO requestDTO) {
		Customer customer = requestDTO.getCustomer();
		List<RoomRequest> roomReqDetails = null;
		log.info("getMyRequestsList starts...");
		try {
			roomReqDetails = roomService.getMyRequestsList(customer);
			requestDTO.setRoomRequestList(roomReqDetails);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in getMyRequestsList : " + ex);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = ROOM_REQUEST_VIEW_ALL)
	public RequestDTO getAllRoomRequests() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getAllRoomRequests starts...");
		try {
			Iterable<RoomRequest> roomReqList = roomService.getUnallocatedRoomRequests();
			requestDTO.setRoomRequestList(
					StreamSupport.stream(roomReqList.spliterator(), false).collect(Collectors.toList()));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getAllRoomRequests : " + ex.getMessage());
		}
		return requestDTO;
	}

	@RequestMapping(value = ROOM_REQUEST_FEASIBLE, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO viewFeasibleRooms(@RequestBody RequestDTO requestDTO) {
		int roomRequestId = requestDTO.getRoomRequest().getRequestId();
		log.info("viewFeasibleRooms starts...");
		try {
			RoomRequest roomRequest = roomService.getRoomRequestById(roomRequestId);
			Room room = util.constructRoomFromRequest(roomRequest);
			Iterable<Room> roomIterables = roomService.findSimilarRooms(room);
			List<Room> roomList = StreamSupport.stream(roomIterables.spliterator(), false).collect(Collectors.toList());
			roomList.sort(Comparator.comparing(Room::getRoomNumber));
			requestDTO.setRoomsList(roomList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewFeasibleRooms : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_REQUEST_ASSIGN_ROOM)
	public RequestDTO assignRoomToRequest(@RequestBody RequestDTO requestDTO) {
		RoomRequest roomRequest = requestDTO.getRoomRequest();
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
			rewardPointsService.allocateRewardPoints(user, ROOM_BOOKING);
			MailDTO mailDTO = new MailDTO();
			mailDTO.setEmailAddress(user.getUserMail());
			mailDTO.setTemplateName(TEMPLATE_ALLOCATION_MAIL);
			String mailStatus = mailUtil.triggerMail(mailDTO);
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
