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
import com.postang.domain.Room;
import com.postang.model.RequestDTO;
import com.postang.service.RoomService;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class RoomController implements RequestMappings, Constants {

	@Autowired
	RoomService roomService;

	/*******************
	 * Room Operations**
	 *******************/

	@PostMapping(value = ROOM_CREATE)
	public RequestDTO createRoom(@RequestBody RequestDTO requestDTO) {
		try {
			Room room = roomService.saveRoom(requestDTO.getRoom());
			requestDTO.setRoom(room);
			requestDTO.setActionStatus(room.getRoomNumber() > 0 ? RM_CRT_SXS : RM_CRT_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_CREATE_MULTIPLE)
	public RequestDTO createMultipleRooms(@RequestBody RequestDTO requestDTO) {
		int countOfRooms = requestDTO.getCountOfRooms() == 0 ? 1 : requestDTO.getCountOfRooms();
		Room room = requestDTO.getRoom();
		try {
			roomService.saveMultipleRooms(room, countOfRooms);
			requestDTO.setActionStatus(RM_CRT_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@RequestMapping(value = ROOM_VIEW_BY_STATUS, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getRoomsByStatus(@RequestBody RequestDTO requestDTO) {
		String roomStatus = requestDTO.getRoomStatus();
		try {
			requestDTO.setRoomsList(roomService.getRoomsByStatus(roomStatus));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_UPDATE)
	public RequestDTO updateRoom(@RequestBody RequestDTO requestDTO) {
		try {
			Room room = roomService.saveRoom(requestDTO.getRoom());
			requestDTO.setRoom(room);
			requestDTO.setActionStatus(room.getRoomNumber() > 0 ? RM_UPDATE_SXS : RM_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@GetMapping(value = ROOM_VIEW_ALL)
	public RequestDTO getAllRooms() {
		RequestDTO requestDTO = new RequestDTO();
		try {
			List<Room> roomList = roomService.getAllRooms();
			roomList.sort(Comparator.comparing(Room::getRoomNumber));
			requestDTO.setRoomsList(roomList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

}
