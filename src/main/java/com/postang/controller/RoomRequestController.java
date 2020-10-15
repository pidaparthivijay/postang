/**
 * 
 */
package com.postang.controller;

import java.util.Comparator;
import java.util.List;

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
import com.postang.domain.Customer;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.model.RequestDTO;
import com.postang.service.RewardPointsService;
import com.postang.service.RoomRequestService;
import com.postang.service.RoomService;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class RoomRequestController implements RequestMappings, Constants {

	@Autowired
	RoomRequestService roomRequestService;

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
		try {
			roomRequest = roomRequestService.requestRoom(roomRequest);
			requestDTO.setActionStatus(roomRequest.getRequestId() > 0 ? ROOM_BOOK_SXS : ROOM_BOOK_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@RequestMapping(value = CUSTOMER_CANCEL_ROOM, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO cancelRequest(@RequestBody RequestDTO requestDTO) {
		int roomRequestId = requestDTO.getRoomRequest().getRequestId();
		try {
			requestDTO.setActionStatus(roomRequestService.cancelRoomRequest(roomRequestId));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@RequestMapping(value = UPDATE_ROOM_REQUEST, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO udpateRoomRequest(@RequestBody RequestDTO requestDTO) {
		RoomRequest roomRequest = requestDTO.getRoomRequest();
		try {
			RoomRequest udpated = roomRequestService.saveRoomRequest(roomRequest);
			requestDTO.setActionStatus(udpated.getRequestId() == roomRequest.getRequestId() ? ROOM_RQST_UPDATE_SXS
					: ROOM_RQST_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@RequestMapping(value = CUSTOMER_GET_ALL_REQUESTS, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getMyRequestsList(@RequestBody RequestDTO requestDTO) {
		Customer customer = requestDTO.getCustomer();
		List<RoomRequest> roomReqDetails = null;
		try {
			roomReqDetails = roomRequestService.getMyRequestsList(customer);
			requestDTO.setRoomRequestList(roomReqDetails);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = ROOM_REQUEST_VIEW_ALL)
	public RequestDTO getAllRoomRequests() {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setRoomRequestList(roomRequestService.getUnallocatedRoomRequests());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@RequestMapping(value = ROOM_REQUEST_FEASIBLE, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO viewFeasibleRooms(@RequestBody RequestDTO requestDTO) {
		int roomRequestId = requestDTO.getRoomRequest().getRequestId();
		try {
			RoomRequest roomRequest = roomRequestService.getRoomRequestByRequestId(roomRequestId);
			Room room = util.constructRoomFromRequest(roomRequest);
			List<Room> roomList = roomService.findSimilarRooms(room);
			roomList.sort(Comparator.comparing(Room::getRoomNumber));
			requestDTO.setRoomsList(roomList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_REQUEST_ASSIGN_ROOM)
	public RequestDTO assignRoomToRequest(@RequestBody RequestDTO requestDTO) {
		RoomRequest roomRequest = requestDTO.getRoomRequest();
		try {

			String assignStatus = roomRequestService.assignRoom(roomRequest);

			requestDTO.setActionStatus(assignStatus);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = ROOM_CLEANUP)
	public RequestDTO cleanUpRooms() {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setActionStatus(roomRequestService.cleanUpRooms());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}
}
