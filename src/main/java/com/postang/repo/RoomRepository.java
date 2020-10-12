/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Room;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByRoomStatus(String roomStatus);

	List<Room> findByRoomType(String roomType);

	List<Room> findByFloorNumber(int floorNumber);

	List<Room> findByRoomModel(String roomModel);

	List<Room> findByRoomCategory(String roomCategory);
	
	List<Room> findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(String roomModel, String roomType,
			String roomCategory,
			String roomStatus);

	Room findByRoomNumber(int roomNumber);

	List<Room> findByRoomRequestId(int requestId);

}
