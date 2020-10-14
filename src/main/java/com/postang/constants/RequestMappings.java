/**
 * 
 */
package com.postang.constants;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RequestMappings {
	public static final String AMENITY_CREATE = "/createAmenity";
	public static final String AMENITY_DELETE_TOGGLE = "/toggleDeleteAmenity";
	public static final String AMENITY_UPDATE = "/updateAmenity";
	public static final String CUSTOMER_AMENITY_REQUEST = "/requestAmenity";
	public static final String AMENITY_VIEW_ALL = "/viewAllAmenities";
	public static final String BRW = "/brw";
	public static final String CUSTOMER_CANCEL_ROOM = "/cancelRequest";
	public static final String CUSTOMER_DETAILS = "/getCustomerDetails";
	public static final String CUSTOMER_GET_ALL_REQUESTS = "/getMyRequestsList";
	public static final String CUSTOMER_REGISTER = "/registerCustomer";
	public static final String CUSTOMER_VIEW_RWD_POINTS = "/viewRewardPoints";
	public static final String EMP_CREATE = "/createEmployee";
	public static final String EMPLOYEE_MAIL_BILL = "/mailBill";
	public static final String EMP_VIEW_ALL = "/getAllEmployees";
	public static final String EMPLOYEE_UPDATE = "/udpateEmployee";
	public static final String EMPLOYEE_VIEW_DETAILS = "/viewEmployeeDetails";
	public static final String LOGIN = "/login";
	public static final String LOOKUP_CREATE = "/createLookup";
	public static final String LOOKUP_DELETE_TOGGLE = "/toggleDeleteLookup";
	public static final String LOOKUP_EXCEL_UPLOAD = "/uploadLookupExcel";
	public static final String LOOKUP_UPDATE = "/updateLookup";
	public static final String LOOKUP_VIEW_ALL = "/viewLookupList";
	public static final String LOOKUP_VIEW_BY_DEF = "/getLookupListByDefinition";
	public static final String LOOKUP_VIEW_DEF = "/getLookupDefs";
	public static final String OTP_REQUEST = "/requestOTP";
	public static final String OTP_SUBMIT = "/submitOtp";
	public static final String PENDING_BILL_VIEW = "/getPendingBillRequests";
	public static final String PENDING_BILL_PDF = "/generatePDF";
	public static final String RESET_PWD = "/resetPwd";
	public static final String ROOM_CREATE = "/createRoom";
	public static final String ROOM_CREATE_MULTIPLE = "/createRoomMultiple";
	public static final String ROOM_REQUEST_ASSIGN_ROOM = "/assignRoomToRequest";
	public static final String ROOM_REQUEST_CREATE = "/requestRoom";
	public static final String ROOM_REQUEST_FEASIBLE = "/viewFeasibleRooms";
	public static final String ROOM_REQUEST_VIEW_ALL = "/getAllRoomRequests";
	public static final String ROOM_UPDATE = "/updateRoom";
	public static final String ROOM_VIEW_ALL = "/getAllRooms";
	public static final String ROOM_VIEW_BY_STATUS = "/getRoomsByStatus";
	public static final String TOUR_PKG_BOOK = "/bookTourPackage";
	public static final String TOUR_PKG_CREATE = "/createTourPackage";
	public static final String TOUR_PKG_DELETE_TOGGLE = "/toggleDeleteTourPackage";
	public static final String TOUR_PKG_UPDATE = "/updateTourPackage";
	public static final String TOUR_PKG_VIEW_ALL = "/viewAllTourPackages";
	public static final String VEHICLE_CREATE = "/createVehicle";
	public static final String VEHICLE_UPDATE = "/updateVehicle";
	public static final String VEHICLE_DELETE_TOGGLE = "/toggleDeleteVehicle";
	public static final String VEHICLE_VIEW_ALL = "/viewAllVehicles";
	public static final String DRIVER_CREATE = "/createDriver";
	public static final String DRIVER_UPDATE = "/updateDriver";
	public static final String DRIVER_DELETE_TOGGLE = "/toggleDeleteDriver";
	public static final String DRIVER_VIEW_ALL = "/viewAllDrivers";
	public static final String TOUR_BKNG_VIEW_ALL = "/viewAllTourBookings";
	public static final String TOUR_BKNG_ASSIGN = "/assignVehDriTour";
	public static final String FEASIBLE_VEHICLES_DRIVERS = "/viewFeasibleVehiclesDrivers";

}
