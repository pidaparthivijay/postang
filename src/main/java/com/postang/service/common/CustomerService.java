/**
 * 
 */
package com.postang.service.common;

import com.postang.model.Customer;
import com.postang.model.Employee;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerService {


	public Iterable<Customer> findAll();	

	public Object findById(long id);

	public Customer getCustomerByUserName(String userName);
	
	public Customer getCustomerDetails(Customer customer);
	
	public Employee getEmployeeByUserName(String userName);

	public Customer saveCustomer(Customer customer);


}
