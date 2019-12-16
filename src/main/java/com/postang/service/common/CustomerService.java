/**
 * 
 */
package com.postang.service.common;

import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.RoomRequest;

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

}
