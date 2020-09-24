/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Amenity;
import com.postang.model.Employee;
import com.postang.model.Lookup;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackage;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface AdminService {

	Room saveRoom(Room room);
	
	Iterable<Room> saveMultipleRooms(List<Room> roomsList);

	Iterable<Room> getAllRooms();

	Iterable<Room> getRoomsByStatus(String roomStatus);

	Iterable<Room> getRoomsByFloor(int floorNumber);

	Iterable<Room> getRoomsByType(String roomType);

	Iterable<Room> getRoomsByModel(String roomModel);

	Iterable<Room> getRoomsByCategory(String roomCategory);

	Iterable<RoomRequest> getAllRoomRequests();
	
	Iterable<RoomRequest> getUnallocatedRoomRequests();

	RoomRequest getRoomRequestById(int roomRequestId);

	Iterable<Room> findSimilarRooms(Room room);

	Employee createEmployee(Employee employee);

	Amenity saveAmenity(Amenity amenity);

	Iterable<Amenity> viewAllAmenities();

	TourPackage saveTourPackage(TourPackage tourPackage);
	
	Iterable<TourPackage> viewAllTourPackages();

	TourPackage findTourPackageByTourPackageName(String tourPackageName);

	Amenity findAmenityByAmenityName(String amenityName);

	Iterable<Lookup> saveLookups(List<Lookup> lookupList);

	Iterable<Lookup> getLookupList();

	Lookup findLookupByLookupId(long lookupId);

	Lookup saveLookup(Lookup lookup);

	Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName);

}
