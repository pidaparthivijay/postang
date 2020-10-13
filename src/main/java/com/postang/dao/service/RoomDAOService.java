/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.Room;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomDAOService {

	List<Room> findSimilarRooms(Room room);

	List<Room> getAllRooms();

	Room getRoomByRoomNumber(int roomNumber);

	List<Room> getRoomsByCategory(String roomCategory);

	List<Room> getRoomsByFloor(int floorNumber);

	List<Room> getRoomsByModel(String roomModel);

	List<Room> getRoomsByStatus(String roomStatus);

	List<Room> getRoomsByType(String roomType);

	Room saveRoom(Room room);

}
