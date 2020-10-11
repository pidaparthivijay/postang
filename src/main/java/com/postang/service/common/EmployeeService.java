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

	Employee getEmployeeByUserName(String userName);

	Employee createEmployee(Employee employee);

	Iterable<Employee> getAllEmployees();

	Employee getEmployeeDetails(String userName);
}
