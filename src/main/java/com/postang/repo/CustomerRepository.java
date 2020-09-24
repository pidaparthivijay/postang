/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Customer;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	public List<Customer> findByUserName(String userName);
}
