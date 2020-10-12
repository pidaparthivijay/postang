/**
 * 
 */
package com.postang.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.domain.Room;
import com.postang.domain.User;
import com.postang.repo.RoomRepository;
import com.postang.repo.UserRepository;
import com.postang.service.RoomService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */

@Service
@Log4j2
public class RoomServiceImpl implements RoomService, Constants {

	@Autowired
	RoomRepository roomRepo;

	@Autowired
	UserRepository userRepo;

	@Override
	public Iterable<Room> findSimilarRooms(Room room) {
		log.info(room);
		return roomRepo.findByRoomModelAndRoomTypeAndRoomCategoryAndRoomStatus(room.getRoomModel(), room.getRoomType(),
				room.getRoomCategory(), VACANT);
	}

	@Override
	public Iterable<Room> getAllRooms() {
		return roomRepo.findAll();
	}

	@Override
	public Room getRoomByRoomNumber(int roomNumber) {
		return roomRepo.findByRoomNumber(roomNumber);
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
	public User getUserById(int userId) {
		return userRepo.findByUserId(userId);
	}

	@Override
	public void saveMultipleRooms(Room room, int count) {
		this.roomCreation(count, room, 1);
	}

	@Override
	public Room saveRoom(Room room) {
		return roomRepo.save(room);
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

	private void roomCreation(int count, Room roomObject, int floorNumber) {
		// A floor can have only 15 rooms
		Boolean checkRequired = false;
		Map<Integer, Integer> unfilledMap = this.getUnfilledFloors();
		List<Integer> floorsList = new ArrayList<>(unfilledMap.keySet());
		if (floorsList.contains(floorNumber)) {
			checkRequired = true;
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
				if (sngSteAc.size() < FC_SNG_STE_AC
						&& (checkRequired || ((sngSteAc.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (sngDlxAc.size() < FC_SNG_DLX_AC
						&& (checkRequired || ((sngDlxAc.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (dblDlxAc.size() < FC_DBL_DLX_AC
						&& (checkRequired || ((dblDlxAc.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (sngSteNac.size() < FC_SNG_STE_NAC
						&& (checkRequired || ((sngSteNac.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (sngDlxNac.size() < FC_SNG_DLX_NAC
						&& (checkRequired || ((sngDlxNac.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (dblSteNac.size() < FC_DBL_STE_NAC
						&& (checkRequired || ((dblSteNac.size() + count) < unfilledMap.get(floorNumber)))) {
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
				if (dblDlxNac.size() < FC_DBL_DLX_NAC
						&& (checkRequired || ((dblDlxNac.size() + count) < unfilledMap.get(floorNumber)))) {
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

}
