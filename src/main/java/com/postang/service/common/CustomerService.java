/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.BillPendingRequest;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackageRequest;

/**
 * @author Vijay
 *
 */
public interface CustomerService {

	public Customer saveCustomer(Customer customer);

	public Object findById(long id);

	public Iterable<Customer> findAll();	

	public Customer getCustomerByUserName(String userName);

	public Customer getCustomerDetails(Customer customer);

	public Employee getEmployeeByUserName(String userName);

	public RoomRequest requestRoom(RoomRequest roomRequest);

	public List<RoomRequest> getMyRequestsList(Customer customer);

	public String cancelRoomRequest(int roomRequestId);

	public double generateBill(String custEmail);

	public List<BillPendingRequest> getPendingBillRequests(String custEmail);

	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest);

	Iterable<TourPackageRequest> getAllTourPackageBookings();

}
