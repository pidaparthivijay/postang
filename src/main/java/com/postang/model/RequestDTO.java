package com.postang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class RequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247577259462744510L;
	
	private int requestId;
	private int userId;
	private String name;
	private Room room;
	private String roomStatus;
	private int floorNumber;
	private Customer customer;
	private int countOfRooms;
	private User user;
	private Employee employee;
	private TourPackage tourPackage;
	private TourPackageRequest tourPackageRequest;
	private Amenity amenity;
	private List<Amenity> amenityList= new ArrayList<>();
	private List<AmenityRequest> amenityRequestList= new ArrayList<>();
	private AmenityRequest amenityRequest;
	private RoomRequest roomRequest;
	private List<Room> roomsList= new ArrayList<>();
	private List<RoomRequest> roomRequestList= new ArrayList<>();
	private List<TourPackage> tourPackageList= new ArrayList<>();
	private List<TourPackageRequest> tourPackageRequestList= new ArrayList<>();
	private String billStatus;
	private String actionStatus;
	private String lookupDefinitionName;
	private Lookup lookup;
	private List<Lookup> lookupList= new ArrayList<>();
	private List<String> lookupDefsList= new ArrayList<>();
	private List<RewardPoints> rewardPointsList= new ArrayList<>();
	private MultipartFile lookupExcel;
	private int roomRequestId;
	private List<PendingBillRequest> pendingBillRequests= new ArrayList<>();
}
