/**
 * 
 */
package com.postang.service.common;

import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomService {
	RoomRequest getRoomRequestByRequestId(int requestId);

	Room getRoomByRoomNumber(int roomNumber);

	RoomRequest saveRoomRequest(RoomRequest roomReq);

	Room saveRoom(Room room);

	User getUserById(int userId);
	
}
