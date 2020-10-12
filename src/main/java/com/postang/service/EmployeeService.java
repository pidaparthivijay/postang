/**
 * 
 */
package com.postang.service;

import com.postang.domain.Employee;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeService {

	Employee getEmployeeByUserName(String userName);

	Employee createEmployee(Employee employee);

	Iterable<Employee> getAllEmployees();

	Employee getEmployeeDetails(String userName);
}
