package com.postang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.postang.domain.Amenity;
import com.postang.domain.AmenityRequest;
import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.Lookup;
import com.postang.domain.PendingBillRequest;
import com.postang.domain.RewardPoints;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.domain.TourPackage;
import com.postang.domain.TourPackageRequest;
import com.postang.domain.User;

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
	
	private String actionStatus;
	private Amenity amenity;
	private List<Amenity> amenityList= new ArrayList<>();
	private AmenityRequest amenityRequest;
	private List<AmenityRequest> amenityRequestList= new ArrayList<>();
	private String billStatus;
	private int countOfRooms;
	private Customer customer;
	private Employee employee;
	private List<Employee> employeesList = new ArrayList<>();
	private int floorNumber;
	private Lookup lookup;
	private String lookupDefinitionName;
	private List<String> lookupDefsList= new ArrayList<>();
	private MultipartFile lookupExcel;
	private List<Lookup> lookupList= new ArrayList<>();
	private String name;
	private List<PendingBillRequest> pendingBillRequests= new ArrayList<>();
	private int requestId;
	private List<RewardPoints> rewardPointsList= new ArrayList<>();
	private Room room;
	private RoomRequest roomRequest;
	private int roomRequestId;
	private List<RoomRequest> roomRequestList= new ArrayList<>();
	private List<Room> roomsList= new ArrayList<>();
	private String roomStatus;
	private String statusMessage;
	private TourPackage tourPackage;
	private List<TourPackage> tourPackageList= new ArrayList<>();
	private TourPackageRequest tourPackageRequest;
	private List<TourPackageRequest> tourPackageRequestList= new ArrayList<>();
	private User user;
	private int userId;
}
