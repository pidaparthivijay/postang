/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.RoomRequest;

/**
 * @author Vijay
 *
 */
public interface RoomRequestRepository extends CrudRepository<RoomRequest, Long> {


	RoomRequest findByRequestId(int roomRequestId);

	List<RoomRequest> findByUserId(int userId);

	Iterable<RoomRequest> findByRoomRequestStatus(String roomRequestStatus);

	

}
