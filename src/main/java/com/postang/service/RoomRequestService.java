/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Customer;
import com.postang.domain.RoomRequest;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRequestService {

	String cancelRoomRequest(int roomRequestId);

	List<RoomRequest> getAllRoomRequests();

	List<RoomRequest> getMyRequestsList(Customer customer);

	RoomRequest getRoomRequestById(int roomRequestId);

	RoomRequest getRoomRequestByRequestId(int requestId);

	List<RoomRequest> getUnallocatedRoomRequests();

	User getUserById(int userId);

	RoomRequest requestRoom(RoomRequest roomRequest);

	RoomRequest saveRoomRequest(RoomRequest roomReq);

	String assignRoom(RoomRequest roomRequest);

}
