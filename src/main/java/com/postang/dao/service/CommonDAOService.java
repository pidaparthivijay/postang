/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CommonDAOService {

	List<Customer> findAll();

	Customer getCustomerByUserName(String userName);
	
	Customer saveCustomer(Customer customer);

	Employee saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeDetails(String userName);

	User saveUser(User user);

	User findUserByUserName(String userName);

}
