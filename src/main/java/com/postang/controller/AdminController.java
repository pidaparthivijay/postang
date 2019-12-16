package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Room;
import com.postang.service.common.AdminService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class AdminController {

	@Autowired
	AdminService adminService;

	MailUtil mailUtil = new MailUtil();

	@RequestMapping(value = "/brw/createRoom", method = { RequestMethod.POST })
	public String createRoom(@RequestBody Room room) {
		String roomJson = "";
		log.info("createRoom starts..." + room);
		try {
			room = adminService.saveRoom(room);
			roomJson = new ObjectMapper().writeValueAsString(room);
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/createRoomMultiple", method = { RequestMethod.POST })
	public String createMultipleRooms(@RequestBody Room room) {
		String roomJson = "";
		log.info("createMultipleRooms starts..." + room);
		try {
			// room = adminService.saveMultipleRooms();
			roomJson = new ObjectMapper().writeValueAsString(room);
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/getAllRooms", method = { RequestMethod.GET })
	public String getAllRooms() {
		String roomJson = "";
		log.info("getAllRooms starts...");
		try {
			Iterable<Room> roomList = adminService.getAllRooms();
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/getRoomsByStatus", method = { RequestMethod.GET,RequestMethod.POST })
	public String getRoomsByStatus(@RequestBody String roomStatus) {
		String roomJson = "";
		log.info("getRoomsByStatus starts..."+roomStatus);
		try {
			Iterable<Room> roomList = adminService.getRoomsByStatus(roomStatus);
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return roomJson;
	}
	
	@RequestMapping(value = "/brw/getRoomsByFloor", method = { RequestMethod.GET,RequestMethod.POST })
	public String getRoomsByFloor(@RequestBody int floorNumber) {
		String roomJson = "";
		log.info("getRoomsByFloor starts..."+floorNumber);
		try {
			Iterable<Room> roomList = adminService.getRoomsByFloor(floorNumber);
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return roomJson;
	}
}
