/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Customer;
import com.postang.domain.Employee;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerService {

	List<Customer> findAll();

	Object findById(long id);

	Customer getCustomerByUserName(String userName);
	
	Customer getCustomerDetails(Customer customer);
	
	Employee getEmployeeByUserName(String userName);

	Customer saveCustomer(Customer customer);


}
