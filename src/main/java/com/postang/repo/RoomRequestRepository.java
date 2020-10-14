/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.RoomRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRequestRepository extends JpaRepository<RoomRequest, Long> {


	RoomRequest findByRequestId(int roomRequestId);

	List<RoomRequest> findByUserId(int userId);

	List<RoomRequest> findByRoomRequestStatus(String roomRequestStatus);

	List<RoomRequest> findByUserName(String userName);
	

}
