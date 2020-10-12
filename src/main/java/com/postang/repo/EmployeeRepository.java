/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Employee;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByUserName(String userName);

}
