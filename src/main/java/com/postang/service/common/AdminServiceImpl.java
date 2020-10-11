/**
 * 
 */
package com.postang.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.model.Amenity;
import com.postang.model.Employee;
import com.postang.model.Lookup;
import com.postang.model.MailDTO;
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
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class AdminServiceImpl implements AdminService, Constants {

	@Autowired
	AmenityRepository amenityRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LookupRepository lookupRepository;

	MailUtil mailUtil = new MailUtil();

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	RoomRequestRepository roomRequestRepo;

	@Autowired
	TourPackageRepository tourPackageRepository;

	@Autowired
	UserRepository userRepository;

	Util util = new Util();

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
				MailDTO mailDTO = new MailDTO();
				mailDTO.setEmployee(emp);
				mailDTO.setTemplateName(TEMPLATE_CUST_SIGN_UP_MAIL);
				String mailStatus = mailUtil.triggerMail(mailDTO);
				log.info(mailStatus);
			}
			return emp;
		} catch (Exception ex) {
			log.info("Exception in createEmployee: " + ex);
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Amenity findAmenityByAmenityName(String amenityName) {
		return amenityRepository.findByAmenityName(amenityName);
	}

	@Override
	public Lookup findLookupByLookupId(long lookupId) {
		return lookupRepository.findByLookupId(lookupId);
	}

	@Override
	public Iterable<Room> findSimilarRooms(Room room) {
		log.info(room);
		return roomRepo.findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(room.getRoomModel(), room.getRoomType(),
				room.getRoomCategory(), VACANT);
	}

	@Override
	public TourPackage findTourPackageByTourPackageName(String tourPackageName) {
		return tourPackageRepository.findByTourPackageName(tourPackageName);
	}

	@Override
	public Iterable<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Iterable<RoomRequest> getAllRoomRequests() {
		return roomRequestRepo.findAll();
	}

	@Override
	public Iterable<Room> getAllRooms() {
		return roomRepo.findAll();
	}

	@Override
	public Iterable<Lookup> getLookupList() {
		return lookupRepository.findAll();
	}

	@Override
	public Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupRepository.findByLookupDefName(lookupDefinitionName);
		}

	@Override
	public RoomRequest getRoomRequestById(int roomRequestId) {
		return roomRequestRepo.findByRequestId(roomRequestId);
	}

	@Override
	public Iterable<Room> getRoomsByCategory(String roomCategory) {
		return roomRepo.findByRoomCategory(roomCategory);
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
	public Iterable<Room> getRoomsByStatus(String roomStatus) {
		return roomRepo.findByRoomStatus(roomStatus);
	}

	@Override
	public Iterable<Room> getRoomsByType(String roomType) {
		return roomRepo.findByRoomType(roomType);
	}

	@Override
	public Iterable<RoomRequest> getUnallocatedRoomRequests() {
		return roomRequestRepo.findByRoomRequestStatus(PENDING);
	}

	@Override
	public Amenity saveAmenity(Amenity amenity) {
		return amenityRepository.save(amenity);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupRepository.save(lookup);
	}

	@Override
	public Iterable<Lookup> saveLookups(List<Lookup> lookupList) {
		return lookupRepository.saveAll(lookupList);

	}

	@Override
	public void saveMultipleRooms(Room room, int count) {
		this.roomCreation(count, room, 1);
	}

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
	}

	@Override
	public TourPackage saveTourPackage(TourPackage tourPackage) {
		return tourPackageRepository.save(tourPackage);
	}

	@Override
	public Iterable<Amenity> viewAllAmenities() {
		return amenityRepository.findAll();
	}

	@Override
	public Iterable<TourPackage> viewAllTourPackages() {
		return tourPackageRepository.findAll();
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
	public Employee getEmployeeDetails(String userName) {
		return employeeRepository.findByUserName(userName).get(0);
	}

	
	
	private void roomCreation(int count, Room roomObject, int floorNumber) {
		// A floor can have only 15 rooms
		Boolean checkRequired=false;
		Map<Integer, Integer> unfilledMap = this.getUnfilledFloors();
		List<Integer> floorsList = new ArrayList<>(unfilledMap.keySet());
		if(floorsList.contains(floorNumber)) {
			checkRequired=true;
		}
		if (count != 0) {
			floorNumber += 1;
			List<Room> roomsInFloor = StreamSupport.stream(this.getRoomsByFloor(floorNumber).spliterator(), false)
					.collect(Collectors.toList());
			List<Room> sngSteAc = roomsInFloor.stream()
					.filter(room -> SINGLE.equalsIgnoreCase(room.getRoomType())
							&& AC.equalsIgnoreCase(room.getRoomCategory())
							&& SUITE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> sngDlxAc = roomsInFloor.stream()
					.filter(room -> SINGLE.equalsIgnoreCase(room.getRoomType())
							&& AC.equalsIgnoreCase(room.getRoomCategory())
							&& DELUXE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> dblSteAc = roomsInFloor.stream()
					.filter(room -> DOUBLE.equalsIgnoreCase(room.getRoomType())
							&& AC.equalsIgnoreCase(room.getRoomCategory())
							&& SUITE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> dblDlxAc = roomsInFloor.stream()
					.filter(room -> DOUBLE.equalsIgnoreCase(room.getRoomType())
							&& AC.equalsIgnoreCase(room.getRoomCategory())
							&& DELUXE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> sngSteNac = roomsInFloor.stream()
					.filter(room -> SINGLE.equalsIgnoreCase(room.getRoomType())
							&& NONAC.equalsIgnoreCase(room.getRoomCategory())
							&& SUITE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> sngDlxNac = roomsInFloor.stream()
					.filter(room -> SINGLE.equalsIgnoreCase(room.getRoomType())
							&& NONAC.equalsIgnoreCase(room.getRoomCategory())
							&& DELUXE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> dblSteNac = roomsInFloor.stream()
					.filter(room -> DOUBLE.equalsIgnoreCase(room.getRoomType())
							&& NONAC.equalsIgnoreCase(room.getRoomCategory())
							&& SUITE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());
			List<Room> dblDlxNac = roomsInFloor.stream()
					.filter(room -> DOUBLE.equalsIgnoreCase(room.getRoomType())
							&& NONAC.equalsIgnoreCase(room.getRoomCategory())
							&& DELUXE.equalsIgnoreCase(room.getRoomModel()))
					.collect(Collectors.toList());

			String switchVariable = roomObject.getRoomType() + UNDERSCORE + roomObject.getRoomModel() + UNDERSCORE
					+ roomObject.getRoomCategory();

			switch (switchVariable) {
			case SINGLE + UNDERSCORE + SUITE + UNDERSCORE + AC:
				if (sngSteAc.size() < FC_SNG_STE_AC && (checkRequired || ((sngSteAc.size() + count) < unfilledMap.get(floorNumber)))) {
						Room targetObject = new Room();
						BeanUtils.copyProperties(roomObject, targetObject);
						targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case SINGLE + UNDERSCORE + DELUXE + UNDERSCORE + AC:
				if (sngDlxAc.size() < FC_SNG_DLX_AC && (checkRequired || ((sngDlxAc.size() + count) < unfilledMap.get(floorNumber)))) {
						Room targetObject = new Room();
						BeanUtils.copyProperties(roomObject, targetObject);
						targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case DOUBLE + UNDERSCORE + SUITE + UNDERSCORE + AC:
				if (dblSteAc.size() < FC_DBL_STE_AC
						&& (checkRequired || ((dblSteAc.size() + count) < unfilledMap.get(floorNumber)))) {
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case DOUBLE + UNDERSCORE + DELUXE + UNDERSCORE + AC:
				if (dblDlxAc.size() < FC_DBL_DLX_AC && (checkRequired || ((dblDlxAc.size() + count) < unfilledMap.get(floorNumber)))){
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case SINGLE + UNDERSCORE + SUITE + UNDERSCORE + NONAC:
				if (sngSteNac.size() < FC_SNG_STE_NAC && (checkRequired || ((sngSteNac.size() + count) < unfilledMap.get(floorNumber)))) {
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case SINGLE + UNDERSCORE + DELUXE + UNDERSCORE + NONAC:
				if (sngDlxNac.size() < FC_SNG_DLX_NAC && (checkRequired || ((sngDlxNac.size() + count) < unfilledMap.get(floorNumber)))) {
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case DOUBLE + UNDERSCORE + SUITE + UNDERSCORE + NONAC:
				if (dblSteNac.size() < FC_DBL_STE_NAC && (checkRequired || ((dblSteNac.size() + count) < unfilledMap.get(floorNumber)))) {
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			case DOUBLE + UNDERSCORE + DELUXE + UNDERSCORE + NONAC:
				if (dblDlxNac.size() < FC_DBL_DLX_NAC && (checkRequired || ((dblDlxNac.size() + count) < unfilledMap.get(floorNumber)))) {
					Room targetObject = new Room();
					BeanUtils.copyProperties(roomObject, targetObject);
					targetObject.setFloorNumber(floorNumber);
					roomRepo.save(targetObject);
					this.roomCreation(count - 1, roomObject, floorNumber);
				} else {
					this.roomCreation(count, roomObject, floorNumber + 1);
				}
				break;
			default:
				break;
			}
		}
		}

	private Map<Integer, Integer> getUnfilledFloors() {
		Iterable<Room> roomsIterable = roomRepo.findAll();
		List<Room> roomsList = StreamSupport.stream(roomsIterable.spliterator(), false).collect(Collectors.toList());
		List<Integer> floorsList = roomsList.stream().distinct().map(Room::getFloorNumber).collect(Collectors.toList());
		List<Integer> unfilledFloors = new ArrayList<>();
		Map<Integer, Integer> unfilledMap = new HashMap<>();
		for (Integer floor : floorsList) {
			Integer currentRoomsCount = roomsList.stream().filter(room -> room.getFloorNumber() == floor)
					.collect(Collectors.toList()).size();
			if (currentRoomsCount < FC_TOTAL) {
				unfilledFloors.add(floor);
				unfilledMap.put(floor, FC_TOTAL - currentRoomsCount);
			}
		}
		return unfilledMap;
	}
		
	}

