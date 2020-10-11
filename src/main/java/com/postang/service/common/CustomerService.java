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

	Iterable<Customer> findAll();

	Object findById(long id);

	Customer getCustomerByUserName(String userName);
	
	Customer getCustomerDetails(Customer customer);
	
	Employee getEmployeeByUserName(String userName);

	Customer saveCustomer(Customer customer);


}
