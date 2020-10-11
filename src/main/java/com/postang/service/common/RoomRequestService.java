/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Customer;
import com.postang.model.RoomRequest;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRequestService {

	String cancelRoomRequest(int roomRequestId);


	Iterable<RoomRequest> getAllRoomRequests();

	List<RoomRequest> getMyRequestsList(Customer customer);

//	Room getRoomByRoomNumber(int roomNumber);
//
	RoomRequest getRoomRequestById(int roomRequestId);

	RoomRequest getRoomRequestByRequestId(int requestId);

//	Iterable<Room> getRoomsByCategory(String roomCategory);
//
//	Iterable<Room> getRoomsByFloor(int floorNumber);
//
//	Iterable<Room> getRoomsByModel(String roomModel);
//
//	Iterable<Room> getRoomsByStatus(String roomStatus);
//
//	Iterable<Room> getRoomsByType(String roomType);

	Iterable<RoomRequest> getUnallocatedRoomRequests();

	User getUserById(int userId);

	RoomRequest requestRoom(RoomRequest roomRequest);

//	Room saveRoom(Room room);

	RoomRequest saveRoomRequest(RoomRequest roomReq);

//	void saveMultipleRooms(Room room, int count);
	
}
