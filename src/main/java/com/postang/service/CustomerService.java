/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Customer;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerService {

	List<Customer> findAll();

	Customer getCustomerByUserName(String userName);
	
	Customer getCustomerDetails(Customer customer);
	
	Customer saveCustomer(Customer customer);


}
