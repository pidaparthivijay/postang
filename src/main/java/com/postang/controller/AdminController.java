package com.postang.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Amenity;
import com.postang.model.AmenityRequest;
import com.postang.model.Constants;
import com.postang.model.Employee;
import com.postang.model.Lookup;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackage;
import com.postang.service.common.AdminService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class AdminController implements Constants {

	@Autowired
	AdminService adminService;

	MailUtil mailUtil = new MailUtil();

	Util util = new Util();

	@PostMapping(value = "/brw/createRoom")
	public String createRoom(@RequestBody Room room) {
		String roomJson = "";
		log.info("createRoom starts..." + room);
		try {
			room = adminService.saveRoom(room);
			roomJson = new ObjectMapper().writeValueAsString(room);
		} catch (Exception ex) {
			log.error("Exception in createRoom : " + ex.getMessage());
		}
		return roomJson;
	}

	@PostMapping(value = "/brw/createRoomMultiple")
	public String createMultipleRooms(@RequestBody Room room) {
		String roomJson = "";
		log.info("createMultipleRooms starts with count of rooms: " + room.getCountOfRooms());
		try {
			List<Room> roomList = new ArrayList<>();
			for (int i = 0; i < room.getCountOfRooms(); i++) {
				Room roomi = new Room();
				BeanUtils.copyProperties(room, roomi);
				roomList.add(roomi);
			}
			Iterable<Room> roomSave = adminService.saveMultipleRooms(roomList);
			roomJson = new ObjectMapper().writeValueAsString(roomSave);
		} catch (Exception ex) {
			log.error("Exception in createMultipleRooms : " + ex.getMessage());
		}
		return roomJson;
	}

	@GetMapping(value = "/brw/getAllRooms")
	public String getAllRooms() {
		String roomJson = "";
		log.info("getAllRooms starts...");
		try {
			Iterable<Room> roomList = adminService.getAllRooms();
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.error("Exception in getAllRooms : " + ex.getMessage());
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/getRoomsByStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public String getRoomsByStatus(@RequestBody String roomStatus) {
		String roomJson = "";
		log.info("getRoomsByStatus starts..." + roomStatus);
		try {
			Iterable<Room> roomList = adminService.getRoomsByStatus(roomStatus);
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.error("Exception in getRoomsByStatus : " + ex.getMessage());
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/getRoomsByFloor", method = { RequestMethod.GET, RequestMethod.POST })
	public String getRoomsByFloor(@RequestBody int floorNumber) {
		String roomJson = "";
		log.info("getRoomsByFloor starts..." + floorNumber);
		try {
			Iterable<Room> roomList = adminService.getRoomsByFloor(floorNumber);
			roomJson = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.error("Exception in getRoomsByFloor : " + ex.getMessage());
		}
		return roomJson;
	}

	@RequestMapping(value = "/brw/getAllRoomRequests", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAllRoomRequests() {
		String roomRequestList = "";
		log.info("getAllRoomRequests starts...");
		try {
			Iterable<RoomRequest> roomReqList = adminService.getUnallocatedRoomRequests();
			roomRequestList = new ObjectMapper().writeValueAsString(roomReqList);
		} catch (Exception ex) {
			log.error("Exception in getAllRoomRequests : " + ex.getMessage());
			ex.printStackTrace();
		}
		return roomRequestList;
	}

	@RequestMapping(value = "/brw/viewFeasibleRooms", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewFeasibleRooms(@RequestBody int roomRequestId) {
		String roomsList = "";
		log.info("viewFeasibleRooms starts...");
		try {
			RoomRequest roomRequest = adminService.getRoomRequestById(roomRequestId);
			Room room = new Room();
			room = util.constructRoomFromRequest(roomRequest);
			Iterable<Room> roomList = adminService.findSimilarRooms(room);
			roomsList = new ObjectMapper().writeValueAsString(roomList);
		} catch (Exception ex) {
			log.error("Exception in viewFeasibleRooms : " + ex.getMessage());
			ex.printStackTrace();
		}
		return roomsList;
	}

	@PostMapping(value = "/brw/createEmployee")
	public String createEmployee(@RequestBody Employee employee) {
		String employeeString = "";
		log.info("createEmployee starts...");
		try {
			Employee emp = adminService.createEmployee(employee);
			employeeString = new ObjectMapper().writeValueAsString(emp);

		} catch (Exception ex) {
			log.error("Exception in viewFeasibleRooms : " + ex.getMessage());
			ex.printStackTrace();
		}
		return employeeString;
	}

	@PostMapping(value = "/brw/createAmenity")
	public String createAmenity(@RequestBody Amenity amenity) {
		String amenityJson = "";
		log.info("createAmenity starts..." + amenity);
		try {
			amenity.setDeleted(NO);
			amenity = adminService.saveAmenity(amenity);
			amenity.setActionStatus(true);
			amenityJson = new ObjectMapper().writeValueAsString(amenity);
		} catch (Exception ex) {
			log.error("Exception in createAmenity : " + ex.getMessage());
		}
		return amenityJson;
	}

	@GetMapping(value = "/brw/viewAllAmenities")
	public String viewAllAmenities() {
		String amenityJson = "";
		log.info("viewAllAmenities starts...");
		try {
			Iterable<Amenity> amenitiesList = adminService.viewAllAmenities();
			amenityJson = new ObjectMapper().writeValueAsString(amenitiesList);
		} catch (Exception ex) {
			log.error("Exception in viewAllAmenities : " + ex.getMessage());
		}
		return amenityJson;
	}

	@PostMapping(value = "/brw/requestAmenity")
	public String requestAmenity(@RequestBody AmenityRequest amenityRequest) {
		String amenityJson = "";
		log.info("requestAmenity starts..." + amenityRequest);
		try {
			amenityRequest = adminService.requestAmenity(amenityRequest);
			amenityRequest.setActionStatus((amenityRequest != null && amenityRequest.getAmenityRequestId() > 0));
			amenityJson = new ObjectMapper().writeValueAsString(amenityRequest);
		} catch (Exception ex) {
			log.error("Exception in requestAmenity: " + ex.getMessage());
		}
		return amenityJson;
	}

	@PostMapping(value = "/brw/createTourPackage")
	public String createTourPackage(@RequestBody TourPackage tourPackage) {
		String tourPackageJson = "";
		log.info("createTourPackage starts..." + tourPackage);
		try {
			tourPackage.setDeleted(NO);
			tourPackage.setTourPackageName(util.generateName(tourPackage));
			tourPackage = adminService.saveTourPackage(tourPackage);
			tourPackage.setActionStatus(true);
			tourPackageJson = new ObjectMapper().writeValueAsString(tourPackage);
		} catch (Exception ex) {
			log.error("Exception in createTourPackage : " + ex.getMessage());
		}
		return tourPackageJson;
	}

	@GetMapping(value = "/brw/viewAllTourPackages")
	public String viewAllTourPackages() {
		String tourPackagesJson = "";
		log.info("viewAllTourPackages starts...");
		try {
			Iterable<TourPackage> tourPackagesList = adminService.viewAllTourPackages();
			tourPackagesJson = new ObjectMapper().writeValueAsString(tourPackagesList);
		} catch (Exception ex) {
			log.error("Exception in viewAllTourPackages : " + ex.getMessage());
		}
		return tourPackagesJson;
	}

	@PostMapping(value = "/brw/toggleDeleteTourPackage")
	public String toggleDeleteTourPackage(@RequestBody String tourPackageName) {
		String tourPackageJson = "";
		log.info("toggleDeleteTourPackage starts..." + tourPackageName);
		try {
			TourPackage tourPackage = adminService.findTourPackageByTourPackageName(tourPackageName);
			tourPackage.setDeleted(YES.equals(tourPackage.getDeleted()) ? NO : YES);
			TourPackage savedTourPackage = adminService.saveTourPackage(tourPackage);
			tourPackageJson = new ObjectMapper().writeValueAsString(savedTourPackage);
			return viewAllTourPackages();
		} catch (Exception ex) {
			log.error("Exception in toggleDeleteTourPackage : " + ex.getMessage());
		}
		return tourPackageJson;
	}

	@PostMapping(value = "/brw/updatePriceAmenity")
	public String updatePriceAmenity(@RequestBody Amenity amenity) {
		String tourPackageJson = "";
		log.info("updatePriceAmenity starts..." + amenity);
		try {
			Amenity existingAmenity = adminService.findAmenityByAmenityName(amenity.getAmenityName());
			existingAmenity.setPrice(amenity.getPrice());
			amenity = adminService.saveAmenity(existingAmenity);
			amenity.setActionStatus(true);
			tourPackageJson = new ObjectMapper().writeValueAsString(amenity);
			return viewAllAmenities();
		} catch (Exception ex) {
			log.error("Exception in updatePriceAmenity : " + ex.getMessage());
		}
		return tourPackageJson;
	}

	@PostMapping(value = "/brw/toggleDeleteAmenity")
	public String toggleDeleteAmenity(@RequestBody String amenityName) {
		String tourPackageJson = "";
		log.info("toggleDeleteAmenity starts..." + amenityName);
		try {
			Amenity amenity = adminService.findAmenityByAmenityName(amenityName);
			amenity.setDeleted(YES.equals(amenity.getDeleted()) ? NO : YES);
			Amenity savedAmenity = adminService.saveAmenity(amenity);
			tourPackageJson = new ObjectMapper().writeValueAsString(savedAmenity);
			return viewAllAmenities();
		} catch (Exception ex) {
			log.error("Exception in toggleDeleteAmenity : " + ex.getMessage());
		}
		return tourPackageJson;
	}

	@PostMapping(value = "/brw/updatePriceTourPackage")
	public String updatePriceTourPackage(@RequestBody TourPackage tourPackage) {
		String tourPackageJson = "";
		log.info("updatePriceTourPackage starts..." + tourPackage);
		try {
			TourPackage existingTourPackage = adminService
					.findTourPackageByTourPackageName(tourPackage.getTourPackageName());
			existingTourPackage.setPricePerHead(tourPackage.getPricePerHead());
			tourPackage = adminService.saveTourPackage(existingTourPackage);
			tourPackage.setActionStatus(true);
			tourPackageJson = new ObjectMapper().writeValueAsString(tourPackage);
			return viewAllTourPackages();
		} catch (Exception ex) {
			log.error("Exception in updatePriceTourPackage : " + ex.getMessage());
		}
		return tourPackageJson;
	}

	@PostMapping(value = "/brw/uploadLookupExcel")
	public String uploadLookupExcel(@RequestParam("lookupExcel") MultipartFile multipartFile) {
		String uploadLookupExcelJson = "";
		log.info("uploadLookupExcel starts..." + multipartFile);
		try {
			List<Lookup> lookupList = util.generateLookupListFromExcelFile(multipartFile);
			Iterable<Lookup> lookupIterables = adminService.saveLookups(lookupList);
			uploadLookupExcelJson = new ObjectMapper().writeValueAsString(lookupIterables);
		} catch (Exception e) {
			log.error("Exception in uploadLookupExcel" + e);
			e.printStackTrace();
		}
		return uploadLookupExcelJson;
	}

	@GetMapping(value = "/brw/viewLookupList")
	public String viewLookupList() {
		String viewLookupJson = "";
		log.info("viewLookupList starts...");
		try {
			Iterable<Lookup> lookupIterables = adminService.getLookupList();
			viewLookupJson = new ObjectMapper().writeValueAsString(lookupIterables);
		} catch (Exception e) {
			log.error("Exception in uploadLookupExcel" + e);
			e.printStackTrace();
		}
		return viewLookupJson;
	}

	@PostMapping(value = "/brw/toggleDelete")
	public String toggleDelete(@RequestBody long lookupId) {
		String toggleDeleteLookupJson = "";
		log.info("viewLookupList starts..." + lookupId);
		try {
			Lookup lookup = adminService.findLookupByLookupId(lookupId);
			lookup.setDeleted(YES.equals(lookup.getDeleted()) ? NO : YES);
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = adminService.saveLookup(lookup);
			toggleDeleteLookupJson = new ObjectMapper().writeValueAsString(savedLookup);
			return viewLookupList();
		} catch (Exception e) {
			log.error("Exception in toggleDelete" + e);
			e.printStackTrace();
		}
		return toggleDeleteLookupJson;
	}

	@GetMapping(value = "/brw/getLookupDefs")
	public String getLookupDefs() {
		String lookupDefsJson = "";
		log.info("getLookupDefs starts...");
		try {
			Iterable<Lookup> lookupIterables = adminService.getLookupList();
			List<String> lookupDefs = new ArrayList<>(new HashSet<String>(
					StreamSupport.stream(lookupIterables.spliterator(), false).collect(Collectors.toList()).stream()
							.distinct().map(Lookup::getLookupDefName).collect(Collectors.toList())));

			lookupDefsJson = new ObjectMapper().writeValueAsString(lookupDefs);
		} catch (Exception e) {
			log.error("Exception in getLookupDefs" + e);
			e.printStackTrace();
		}
		return lookupDefsJson;
	}

	@PostMapping(value = "/brw/createLookup")
	public String createLookup(@RequestBody Lookup lookup) {
		String createLookupJson = "";
		log.info("createLookup starts..." + lookup);
		try {
			lookup.setDeleted(NO);
			lookup.setCreatedDate(new Date());
			Lookup savedLookup = adminService.saveLookup(lookup);
			createLookupJson = new ObjectMapper().writeValueAsString(savedLookup);
			return createLookupJson;
		} catch (Exception e) {
			log.error("Exception in createLookup" + e);
			e.printStackTrace();
		}
		return createLookupJson;
	}

	@PostMapping(value = "/brw/getLookupListByDefinition")
	public String getLookupListByDefinition(@RequestBody String lookupDefinitionName) {
		String createLookupJson = "";
		log.info("getLookupListByDefinition starts..." + lookupDefinitionName);
		try {
			Iterable<Lookup> lookupList = adminService.getLookupListByDefinition(lookupDefinitionName);
			createLookupJson = new ObjectMapper().writeValueAsString(lookupList);
			return createLookupJson;
		} catch (Exception e) {
			log.error("Exception in getLookupListByDefinition..." + e);
			e.printStackTrace();
		}
		return createLookupJson;
	}
}
