/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.model.Room;
import com.postang.repo.RoomRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@Log4j2
@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	RoomRepository roomRepo;

	Util util = new Util();

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
		}

	@Override
	public List<Room> saveMultipleRooms(List<Room> roomsList) {
		return null;
	}

	@Override
	public Iterable<Room> getAllRooms() {
		return roomRepo.findAll();
	}
	
	@Override
	public Iterable<Room> getRoomsByStatus(String roomStatus) {
		return roomRepo.findByRoomStatus(roomStatus);
	}
	
	@Override
	public Iterable<Room> getRoomsByType(String roomType) {
		return roomRepo.findByRoomType(roomType);
	}
	
	@Override
	public Iterable<Room> getRoomsByFloor(int floorNumber) {
		return roomRepo.findByFloorNumber(floorNumber);
	}
	@Override
	public Iterable<Room> getRoomsByModel(String roomModel) {
		return roomRepo.findByRoomModel(roomModel);
	}
	@Override
	public Iterable<Room> getRoomsByCategory(String roomCategory) {
		return roomRepo.findByRoomCategory(roomCategory);
	}
	
}
