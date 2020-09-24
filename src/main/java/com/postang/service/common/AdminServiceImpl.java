/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.model.Amenity;
import com.postang.model.Constants;
import com.postang.model.Employee;
import com.postang.model.Lookup;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackage;
import com.postang.model.User;
import com.postang.repo.AmenityRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.LookupRepository;
import com.postang.repo.RoomRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.TourPackageRepository;
import com.postang.repo.UserRepository;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@Log4j2
@Service
public class AdminServiceImpl implements AdminService, Constants {

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	RoomRequestRepository roomRequestRepo;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AmenityRepository amenityRepository;

	@Autowired
	TourPackageRepository tourPackageRepository;

	@Autowired
	LookupRepository lookupRepository;

	Util util = new Util();

	MailUtil mailUtil = new MailUtil();

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
	}

	@Override
	public Iterable<Room> saveMultipleRooms(List<Room> roomsList) {
		return roomRepo.saveAll(roomsList);
	}

	@Override
	public Iterable<Room> getAllRooms() {
		return roomRepo.findAll();
	}

	@Override
	public Iterable<Room> getRoomsByStatus(String roomStatus) {
		return roomRepo.findByRoomStatus(roomStatus);
	}

	@Override
	public Iterable<Room> getRoomsByType(String roomType) {
		return roomRepo.findByRoomType(roomType);
	}

	@Override
	public Iterable<Room> getRoomsByFloor(int floorNumber) {
		return roomRepo.findByFloorNumber(floorNumber);
	}

	@Override
	public Iterable<Room> getRoomsByModel(String roomModel) {
		return roomRepo.findByRoomModel(roomModel);
	}

	@Override
	public Iterable<Room> getRoomsByCategory(String roomCategory) {
		return roomRepo.findByRoomCategory(roomCategory);
	}

	@Override
	public Iterable<RoomRequest> getAllRoomRequests() {
		return roomRequestRepo.findAll();
	}

	@Override
	public RoomRequest getRoomRequestById(int roomRequestId) {
		return roomRequestRepo.findByRequestId(roomRequestId);
	}

	@Override
	public Iterable<Room> findSimilarRooms(Room room) {
		log.info(room);
		return roomRepo.findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(room.getRoomModel(), room.getRoomType(),
				room.getRoomCategory(), VACANT);
	}

	@Override
	public Iterable<RoomRequest> getUnallocatedRoomRequests() {
		return roomRequestRepo.findByRoomRequestStatus(PENDING);
	}

	@Override
	public Employee createEmployee(Employee employee) {

		User user = new User();
		try {

			Employee validatedEmployee = this.validate(employee);
			if (!StringUtils.isEmpty(validatedEmployee.getStatusMessage())) {
				return validatedEmployee;
			}
			user = util.generateUserFromEmployee(employee);
			User newUser = userRepository.save(user);
			if (newUser == null) {
				Employee newEmp = new Employee();
				newEmp.setEmpId(-1L);
				newEmp.setActionStatus(false);
				newEmp.setStatusMessage(USER_INVALID);
				return newEmp;
			}
			Employee emp = employeeRepository.save(employee);
			if (emp.getEmpId() > 0) {
				emp.setActionStatus(true);
				emp.setStatusMessage("");
				emp.setEmpPass(user.getPassword());
				String mailStatus = mailUtil.sendSignUpMailForEmployee(emp);
				log.info(mailStatus);
			}
			return emp;
		} catch (Exception ex) {
			log.info("Exception in createEmployee: " + ex);
			ex.printStackTrace();
		}
		return null;
	}

	private Employee validate(Employee employee) {
		List<Employee> customerList = null;
		customerList = employeeRepository.findByUserName(employee.getUserName());
		if (!customerList.isEmpty()) {
			Employee newEmp = new Employee();
			newEmp.setEmpId(-1L);
			newEmp.setActionStatus(false);
			newEmp.setStatusMessage(USERNAME_TAKEN);
			return newEmp;
		}
		return employee;
	}

	@Override
	public Amenity saveAmenity(Amenity amenity) {
		return amenityRepository.save(amenity);
	}

	@Override
	public Iterable<Amenity> viewAllAmenities() {
		return amenityRepository.findAll();
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageRepository.save(tourPackage);
	}

	@Override
	public Iterable<TourPackage> viewAllTourPackages() {
		return tourPackageRepository.findAll();
	}

	@Override
	public TourPackage findTourPackageByTourPackageName(String tourPackageName) {
		return tourPackageRepository.findByTourPackageName(tourPackageName);
	}

	@Override
	public Amenity findAmenityByAmenityName(String amenityName) {
		return amenityRepository.findByAmenityName(amenityName);
	}

	@Override
	public Iterable<Lookup> saveLookups(List<Lookup> lookupList) {
		return lookupRepository.saveAll(lookupList);

	}

	@Override
	public Iterable<Lookup> getLookupList() {
		return lookupRepository.findAll();
	}

	@Override
	public Lookup findLookupByLookupId(long lookupId) {
		return lookupRepository.findByLookupId(lookupId);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupRepository.save(lookup);
	}

	@Override
	public Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupRepository.findByLookupDefName(lookupDefinitionName);
		}

}
