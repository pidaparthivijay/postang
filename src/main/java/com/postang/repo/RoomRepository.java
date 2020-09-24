/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Room;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

	Iterable<Room> findByRoomStatus(String roomStatus);

	Iterable<Room> findByRoomType(String roomType);

	Iterable<Room> findByFloorNumber(int floorNumber);

	Iterable<Room> findByRoomModel(String roomModel);

	Iterable<Room> findByRoomCategory(String roomCategory);
	
	Iterable<Room> findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(String roomModel, String roomType,String roomCategory,
			String roomStatus);

	Room findByRoomNumber(int roomNumber);

	List<Room> findByRoomRequestId(int requestId);

}
