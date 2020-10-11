/**
 * 
 */
package com.postang.service.common;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.postang.model.AmenityRequest;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.PendingBillRequest;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackageRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerService {

	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest);

	public String cancelRoomRequest(int roomRequestId);

	public Iterable<Customer> findAll();	

	public Object findById(long id);

	public double generateBill(String custEmail);

	public ByteArrayInputStream generatedBillPdf(String custEmail);

	public Iterable<TourPackageRequest> getAllTourPackageBookings();

	public Customer getCustomerByUserName(String userName);
	
	public Customer getCustomerDetails(Customer customer);
	
	public Employee getEmployeeByUserName(String userName);

	public List<RoomRequest> getMyRequestsList(Customer customer);

	public List<PendingBillRequest> getPendingBillRequests(String custEmail);

	public AmenityRequest requestAmenity(AmenityRequest amenityRequest);

	public RoomRequest requestRoom(RoomRequest roomRequest);

	public Customer saveCustomer(Customer customer);

	public String triggerMailBill(String custEmail);

}
