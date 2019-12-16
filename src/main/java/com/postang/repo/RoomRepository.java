/**
 * 
 */
package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Room;

/**
 * @author Vijay
 *
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

	Iterable<Room> findByRoomStatus(String roomStatus);

	Iterable<Room> findByRoomType(String roomType);

	Iterable<Room> findByFloorNumber(int floorNumber);

	Iterable<Room> findByRoomModel(String roomModel);

	Iterable<Room> findByRoomCategory(String roomCategory);
}
