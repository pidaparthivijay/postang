/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Room;

/**
 * @author Vijay
 *
 */
public interface AdminService {

	Room saveRoom(Room room);
	
	List<Room> saveMultipleRooms(List<Room> roomsList);

	Iterable<Room> getAllRooms();

	Iterable<Room> getRoomsByStatus(String roomStatus);

	Iterable<Room> getRoomsByFloor(int floorNumber);

	Iterable<Room> getRoomsByType(String roomType);

	Iterable<Room> getRoomsByModel(String roomModel);

	Iterable<Room> getRoomsByCategory(String roomCategory);

}
