/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Room;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomService {

	List<Room> findSimilarRooms(Room room);

	List<Room> getAllRooms();

	Room getRoomByRoomNumber(int roomNumber);

	List<Room> getRoomsByCategory(String roomCategory);

	List<Room> getRoomsByFloor(int floorNumber);

	List<Room> getRoomsByModel(String roomModel);

	List<Room> getRoomsByStatus(String roomStatus);

	List<Room> getRoomsByType(String roomType);

	User getUserById(int userId);

	Room saveRoom(Room room);

	void saveMultipleRooms(Room room, int count);

}
