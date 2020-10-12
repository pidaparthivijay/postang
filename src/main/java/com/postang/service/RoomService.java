/**
 * 
 */
package com.postang.service;

import com.postang.domain.Room;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomService {

	Iterable<Room> findSimilarRooms(Room room);

	Iterable<Room> getAllRooms();

	Room getRoomByRoomNumber(int roomNumber);

	Iterable<Room> getRoomsByCategory(String roomCategory);

	Iterable<Room> getRoomsByFloor(int floorNumber);

	Iterable<Room> getRoomsByModel(String roomModel);

	Iterable<Room> getRoomsByStatus(String roomStatus);

	Iterable<Room> getRoomsByType(String roomType);

	User getUserById(int userId);

	Room saveRoom(Room room);

	void saveMultipleRooms(Room room, int count);

}
