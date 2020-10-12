/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.Employee;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

	List<Employee> findByUserName(String userName);

}
