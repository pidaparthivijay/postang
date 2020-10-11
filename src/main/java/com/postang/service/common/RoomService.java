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
	Room getRoomByRoomNumber(int roomNumber);

	RoomRequest getRoomRequestByRequestId(int requestId);

	User getUserById(int userId);

	Room saveRoom(Room room);

	RoomRequest saveRoomRequest(RoomRequest roomReq);
	
}
