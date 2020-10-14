/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.RoomRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRequestDAOService {

	List<RoomRequest> getAllRoomRequests();

	RoomRequest getRoomRequestByRequestId(int requestId);

	List<RoomRequest> getRoomRequestByStatus(String status);

	RoomRequest requestRoom(RoomRequest roomRequest);

	RoomRequest saveRoomRequest(RoomRequest roomReq);

	List<RoomRequest> getRequestListByUserId(int userId);

	List<RoomRequest> getRequestListByUserName(String userName);

}
