/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Employee;

/**
 * @author Vijay
 *
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	List<Employee> findByUserName(String userName);

}
