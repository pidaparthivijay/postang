/**
 * 
 */
package com.postang.service.common;

import com.postang.model.Employee;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeService {

	Employee createEmployee(Employee employee);
	Iterable<Employee> getAllEmployees();
	public Employee getEmployeeByUserName(String userName);

	Employee getEmployeeDetails(String userName);
}
