/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.constants.Constants;
import com.postang.dao.service.RoomDAOService;
import com.postang.domain.Room;
import com.postang.repo.RoomRepository;

/**
 * @author Subrahmanya Vijay
 *
 */

@Repository
public class RoomDAOServiceImpl implements RoomDAOService, Constants {

	@Autowired
	RoomRepository roomRepo;

	@Override
	public List<Room> findSimilarRooms(Room room) {
		return roomRepo.findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(room.getRoomModel(), room.getRoomType(),
				room.getRoomCategory(), VACANT);
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepo.findAll();
	}

	@Override
	public Room getRoomByRoomNumber(int roomNumber) {
		return roomRepo.findByRoomNumber(roomNumber);
	}

	@Override
	public List<Room> getRoomsByCategory(String roomCategory) {
		return roomRepo.findByRoomCategory(roomCategory);
	}

	@Override
	public List<Room> getRoomsByFloor(int floorNumber) {
		return roomRepo.findByFloorNumber(floorNumber);
	}

	@Override
	public List<Room> getRoomsByModel(String roomModel) {
		return roomRepo.findByRoomModel(roomModel);
	}

	@Override
	public List<Room> getRoomsByStatus(String roomStatus) {
		return roomRepo.findByRoomStatus(roomStatus);
	}

	@Override
	public List<Room> getRoomsByType(String roomType) {
		return roomRepo.findByRoomType(roomType);
	}

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
	}

	@Override
	public List<Room> findByRoomRequestId(int requestId) {
		return roomRepo.findByRoomRequestId(requestId);
	}

}
