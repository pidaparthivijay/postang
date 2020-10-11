package com.postang.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.model.Amenity;
import com.postang.model.Employee;
import com.postang.model.Lookup;
import com.postang.model.RequestDTO;
import com.postang.model.Room;
import com.postang.model.TourPackage;
import com.postang.service.common.AdminService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class AdminController implements RequestMappings,Constants {

	@Autowired
	AdminService adminService;

	MailUtil mailUtil = new MailUtil();

	Util util = new Util();

	
	/*******************
	 * Room Operations**
	 *******************/
	
	@PostMapping(value = ROOM_CREATE)
	public RequestDTO createRoom(@RequestBody RequestDTO requestDTO) {
		log.info("createRoom starts..." + requestDTO.getRoom());
		try {
			Room room = adminService.saveRoom(requestDTO.getRoom());
			requestDTO.setRoom(room);
			requestDTO.setActionStatus(room.getRoomNumber() > 0 ? RM_CRT_SXS : RM_CRT_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createRoom : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_CREATE_MULTIPLE)
	public RequestDTO createMultipleRooms(@RequestBody RequestDTO requestDTO) {
		int countOfRooms = requestDTO.getCountOfRooms() == 0 ? 1 : requestDTO.getCountOfRooms();
		log.info("createMultipleRooms starts with count of rooms: " + countOfRooms);
		Room room = requestDTO.getRoom();
		try {
			adminService.saveMultipleRooms(room, countOfRooms);
			requestDTO.setActionStatus(RM_CRT_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createMultipleRooms : " + ex.getMessage());
		}
		return requestDTO;
	}

	@RequestMapping(value = ROOM_VIEW_BY_STATUS, method = { RequestMethod.GET, RequestMethod.POST })
	public RequestDTO getRoomsByStatus(@RequestBody RequestDTO requestDTO) {
		String roomStatus = requestDTO.getRoomStatus();
		log.info("getRoomsByStatus starts..." + roomStatus);
		try {
			Iterable<Room> roomList = adminService.getRoomsByStatus(roomStatus);
			requestDTO.setRoomsList(StreamSupport.stream(roomList.spliterator(), false).collect(Collectors.toList()));
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getRoomsByStatus : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = ROOM_UPDATE)
	public RequestDTO updateRoom(@RequestBody RequestDTO requestDTO) {
		log.info("updateRoom starts..." + requestDTO.getRoom());
		try {
			Room room = adminService.saveRoom(requestDTO.getRoom());
			requestDTO.setRoom(room);
			requestDTO.setActionStatus(room.getRoomNumber() > 0 ? RM_UPDATE_SXS : RM_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updateRoom: " + ex.getMessage());
		}
		return requestDTO;
	}

	@GetMapping(value = ROOM_VIEW_ALL)
	public RequestDTO getAllRooms() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getAllRooms starts...");
		try {
			Iterable<Room> roomIterables = adminService.getAllRooms();
			List<Room> roomList = StreamSupport.stream(roomIterables.spliterator(), false).collect(Collectors.toList());
			roomList.sort(Comparator.comparing(Room::getRoomNumber));
			requestDTO.setRoomsList(roomList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getAllRooms : " + ex.getMessage());
		}
		return requestDTO;
	}
	
	
	/***********************
	 * Employee Operations**
	 ***********************/
	
	@PostMapping(value = EMP_CREATE)
	public RequestDTO createEmployee(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("createEmployee starts...");
		try {
			Employee emp = adminService.createEmployee(employee);
			requestDTO.setEmployee(emp);
			requestDTO.setActionStatus((emp != null && emp.getEmpId() > 0) ? EMP_CRT_SXS : EMP_CRT_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createEmployee : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = EMP_VIEW_ALL)
	public RequestDTO getAllEmployees() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getAllEmployees starts...");
		try {
			Iterable<Employee> empIterable = adminService.getAllEmployees();
			List<Employee> empList = StreamSupport.stream(empIterable.spliterator(), false)
					.collect(Collectors.toList());
			empList.sort(Comparator.comparing(Employee::getEmpId));
			requestDTO.setEmployeesList(empList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getAllEmployees : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}
	
	/**********************
	 * Amenity Operations**
	 **********************/
	
	@PostMapping(value = AMENITY_CREATE)
	public RequestDTO createAmenity(@RequestBody RequestDTO requestDTO) {
		Amenity amenity=requestDTO.getAmenity();
		log.info("createAmenity starts..." + amenity);
		try {
			amenity.setDeleted(NO);
			amenity = adminService.saveAmenity(amenity);
			requestDTO.setActionStatus((amenity != null && amenity.getAmenityId() > 0) ? AMNT_CRT_SXS : AMNT_CRT_FAIL);
			return viewAllAmenities(requestDTO.getActionStatus());
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createAmenity : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = AMENITY_UPDATE)
	public RequestDTO updateAmenity(@RequestBody RequestDTO requestDTO) {
		Amenity amenity=requestDTO.getAmenity();
		log.info("updatePriceAmenity starts..." + amenity);
		try {
			amenity = adminService.saveAmenity(amenity);
			requestDTO.setAmenity(amenity);
			return viewAllAmenities(
					(amenity != null && amenity.getAmenityId() > 0) ? AMNT_UPDATE_SXS : AMNT_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updatePriceAmenity : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = AMENITY_DELETE_TOGGLE)
	public RequestDTO toggleDeleteAmenity(@RequestBody RequestDTO requestDTO) {
		String amenityName = requestDTO.getAmenity().getAmenityName();
		log.info("toggleDeleteAmenity starts..." + amenityName);
		try {
			Amenity amenity = adminService.findAmenityByAmenityName(amenityName);
			amenity.setDeleted(YES.equals(amenity.getDeleted()) ? NO : YES);
			Amenity savedAmenity = adminService.saveAmenity(amenity);
			requestDTO.setAmenity(savedAmenity);
			return viewAllAmenities(YES.equals(savedAmenity.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDeleteAmenity : " + ex.getMessage());
		}
		return requestDTO;
	}

	@GetMapping(value = AMENITY_VIEW_ALL)
	public RequestDTO viewAllAmenities(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewAllAmenities starts...");
		try {
			Iterable<Amenity> amenitiesList = adminService.viewAllAmenities();
			requestDTO.setAmenityList(
					StreamSupport.stream(amenitiesList.spliterator(), false).collect(Collectors.toList()));
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewAllAmenities : " + ex.getMessage());
		}
		return requestDTO;
	}

	
	/***************************
	 * Tour package Operations**
	 ***************************/
	
	@PostMapping(value = TOUR_PKG_CREATE)
	public RequestDTO createTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackage tourPackage = requestDTO.getTourPackage();
		log.info("createTourPackage starts..." + tourPackage);
		try {
			tourPackage.setDeleted(NO);
			tourPackage.setTourPackageName(util.generateName(tourPackage));
			tourPackage = adminService.saveTourPackage(tourPackage);
			requestDTO.setActionStatus(TOUR_PKG_CRT_SXS);
			requestDTO.setTourPackage(tourPackage);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = "/brw/updateTourPackage")
	public RequestDTO updateTourPackage(@RequestBody RequestDTO requestDTO) {
		TourPackage tourPackage=requestDTO.getTourPackage();
		log.info("updatePriceTourPackage starts..." + tourPackage);
		try {
			tourPackage = adminService.saveTourPackage(tourPackage);
			tourPackage.setActionStatus(true);
			requestDTO.setTourPackage(tourPackage);
			return viewAllTourPackages(SUCCESS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updatePriceTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}
	
	@GetMapping(value = TOUR_PKG_VIEW_ALL)
	public RequestDTO viewAllTourPackages(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewAllTourPackages starts...");
		try {
			Iterable<TourPackage> tourPackagesList = adminService.viewAllTourPackages();
			requestDTO.setTourPackageList(
					StreamSupport.stream(tourPackagesList.spliterator(), false).collect(Collectors.toList()));
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewAllTourPackages : " + ex.getMessage());
		}
		return requestDTO;
	}

	@PostMapping(value = TOUR_PKG_DELETE_TOGGLE)
	public RequestDTO toggleDeleteTourPackage(@RequestBody RequestDTO requestDTO) {
		String tourPackageName = requestDTO.getTourPackage().getTourPackageName();
		log.info("toggleDeleteTourPackage starts..." + tourPackageName);
		try {
			TourPackage tourPackage = adminService.findTourPackageByTourPackageName(tourPackageName);
			tourPackage.setDeleted(YES.equals(tourPackage.getDeleted()) ? NO : YES);
			TourPackage savedTourPackage = adminService.saveTourPackage(tourPackage);
			requestDTO.setTourPackage(savedTourPackage);
			return viewAllTourPackages(YES.equals(savedTourPackage.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDeleteTourPackage : " + ex.getMessage());
		}
		return requestDTO;
	}


	/********************
	 * Lookup Operations**
	 *********************/

	@PostMapping(value = LOOKUP_EXCEL_UPLOAD)
	public RequestDTO uploadLookupExcel(@RequestParam("lookupExcel") MultipartFile multipartFile) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("uploadLookupExcel starts..." + multipartFile);
		try {
			List<Lookup> lookupList = util.generateLookupListFromExcelFile(multipartFile);
			Iterable<Lookup> lookupIterables = adminService.saveLookups(lookupList);
			requestDTO.setLookupList(
					StreamSupport.stream(lookupIterables.spliterator(), false).collect(Collectors.toList()));
			requestDTO.setActionStatus(LOOKUP_EXCEL_SXS);
			return viewLookupList(LOOKUP_EXCEL_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in uploadLookupExcel" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_CREATE)
	public RequestDTO createLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup =requestDTO.getLookup();
		log.info("createLookup starts..." + lookup);
		try {
			lookup.setDeleted(NO);
			lookup.setCreatedDate(new Date());
			Lookup savedLookup = adminService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(savedLookup.getLookupId() > 0 ? SUCCESS : FAILURE);
		} catch (Exception e) {
			log.error("Exception in createLookup" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}
	
	@PostMapping(value = LOOKUP_UPDATE)
	public RequestDTO updateLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup = requestDTO.getLookup();
		log.info("updateLookup starts..." + lookup);
		try {
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = adminService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(UPDATE_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updateLookup" + e);
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_DELETE_TOGGLE)
	public RequestDTO toggleDelete(@RequestBody RequestDTO requestDTO) {
		long lookupId = requestDTO.getLookup().getLookupId();
		log.info("toggleDelete starts..." + lookupId);
		try {
			Lookup lookup = adminService.findLookupByLookupId(lookupId);
			lookup.setDeleted(YES.equals(lookup.getDeleted()) ? NO : YES);
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = adminService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			return viewLookupList(YES.equals(savedLookup.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDelete" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_DEF)
	public RequestDTO getLookupDefs() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getLookupDefs starts...");
		try {
			Iterable<Lookup> lookupIterables = adminService.getLookupList();
			List<String> lookupDefsList = StreamSupport.stream(lookupIterables.spliterator(), false)
					.collect(Collectors.toList()).stream().distinct().map(Lookup::getLookupDefName)
					.collect(Collectors.toList());
			requestDTO.setLookupDefsList(lookupDefsList.stream().distinct().collect(Collectors.toList()));
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getLookupDefs" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_VIEW_BY_DEF)
	public RequestDTO getLookupListByDefinition(@RequestBody RequestDTO requestDTO) {
		String lookupDefinitionName = requestDTO.getLookupDefinitionName();
		log.info("getLookupListByDefinition starts..." + lookupDefinitionName);
		try {
			Iterable<Lookup> lookupList = adminService.getLookupListByDefinition(lookupDefinitionName);
			requestDTO
					.setLookupList(StreamSupport.stream(lookupList.spliterator(), false).collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("Exception in getLookupListByDefinition..." + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_ALL)
	public RequestDTO viewLookupList(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewLookupList starts...");
		try {
			Iterable<Lookup> lookupIterables = adminService.getLookupList();
			requestDTO.setLookupList(
					StreamSupport.stream(lookupIterables.spliterator(), false).collect(Collectors.toList()));
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewLookupList" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

}
