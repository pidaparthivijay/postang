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

	Employee createEmployee(Employee employee);
	
	Amenity findAmenityByAmenityName(String amenityName);

	Lookup findLookupByLookupId(long lookupId);

	Iterable<Room> findSimilarRooms(Room room);

	TourPackage findTourPackageByTourPackageName(String tourPackageName);

	Iterable<Employee> getAllEmployees();

	Iterable<RoomRequest> getAllRoomRequests();

	Iterable<Room> getAllRooms();

	Iterable<Lookup> getLookupList();
	
	Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName);

	RoomRequest getRoomRequestById(int roomRequestId);

	Iterable<Room> getRoomsByCategory(String roomCategory);

	Iterable<Room> getRoomsByFloor(int floorNumber);

	Iterable<Room> getRoomsByModel(String roomModel);

	Iterable<Room> getRoomsByStatus(String roomStatus);

	Iterable<Room> getRoomsByType(String roomType);
	
	Iterable<RoomRequest> getUnallocatedRoomRequests();

	Amenity saveAmenity(Amenity amenity);

	Lookup saveLookup(Lookup lookup);

	Iterable<Lookup> saveLookups(List<Lookup> lookupList);

	Iterable<Room> saveMultipleRooms(List<Room> roomsList);

	Room saveRoom(Room room);

	TourPackage saveTourPackage(TourPackage tourPackage);

	Iterable<Amenity> viewAllAmenities();

	Iterable<TourPackage> viewAllTourPackages();

}
