/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Customer;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public List<Customer> findByUserName(String userName);
}
