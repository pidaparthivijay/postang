/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Customer;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomService {
	public Room getRoomByRoomNumber(int roomNumber);

	public Iterable<RoomRequest> getAllRoomRequests();

	public RoomRequest getRoomRequestById(int roomRequestId);

	public Iterable<RoomRequest> getUnallocatedRoomRequests();

	public RoomRequest getRoomRequestByRequestId(int requestId);

	void saveMultipleRooms(Room room, int count);

	public String cancelRoomRequest(int roomRequestId);

	public List<RoomRequest> getMyRequestsList(Customer customer);

	public RoomRequest requestRoom(RoomRequest roomRequest);

	public Iterable<Room> findSimilarRooms(Room room);

	public Iterable<Room> getAllRooms();

	public Iterable<Room> getRoomsByCategory(String roomCategory);

	public Iterable<Room> getRoomsByFloor(int floorNumber);

	public Iterable<Room> getRoomsByModel(String roomModel);

	public Iterable<Room> getRoomsByStatus(String roomStatus);

	public Iterable<Room> getRoomsByType(String roomType);

	public User getUserById(int userId);

	public Room saveRoom(Room room);

	public RoomRequest saveRoomRequest(RoomRequest roomReq);
	
}
