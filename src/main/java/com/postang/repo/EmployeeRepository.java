/**
 * 
 */
package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Employee;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByUserName(String userName);

}
