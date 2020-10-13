/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Employee;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeService {

	Employee createEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeDetails(String userName);
}
