/**
 * 
 */
package com.postang.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.User;
import com.postang.repo.CustomerRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.UserRepository;
import com.postang.service.CustomerService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService, Constants {

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	EmployeeRepository empRepo;



	@Autowired
	UserRepository userRepo;

	Util util = new Util();

	@Override
	public List<Customer> findAll() {
		return custRepo.findAll();
	}

	@Override
	public Optional<Customer> findById(long id) {
		return custRepo.findById(id);
	}

	@Override
	public Customer getCustomerByUserName(String userName) {
		List<Customer> custList = null;
		custList = custRepo.findByUserName(userName);
		if (CollectionUtils.isNotEmpty(custList)) {
			return custList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Customer getCustomerDetails(Customer customer) {
		List<Customer> custList = null;
		User user = null;
		custList = custRepo.findByUserName(customer.getUserName());
		user = userRepo.findByUserName(customer.getUserName());
		if (CollectionUtils.isNotEmpty(custList)) {
			custList.get(0).setCustPass(user.getPassword());
			return custList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList = null;
		empList = empRepo.findByUserName(userName);
		if (CollectionUtils.isNotEmpty(empList)) {
			return empList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		User user = new User();
		try {

			Customer validatedCustomer = this.validate(customer);
			if (!StringUtils.isEmpty(validatedCustomer.getStatusMessage())) {
				return validatedCustomer;
			}
			user = util.generateUserFromCustomer(customer);
			User newUser = userRepo.save(user);
			if (newUser == null) {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setStatusMessage(USER_INVALID);
				return newCus;
			}
			return custRepo.save(customer);
		} catch (Exception ex) {
			log.info("Exception in saveCustomer: " + ex);
			ex.printStackTrace();
		}
		return null;

	}

	private Customer validate(Customer customer) {
		List<Customer> customerList = null;
		customerList = custRepo.findByUserName(customer.getUserName());
		if (CollectionUtils.isNotEmpty(customerList)) {
			Customer newCus = new Customer();
			newCus.setCustId(-1L);
			newCus.setActionStatus(false);
			newCus.setStatusMessage(USERNAME_TAKEN);
			return newCus;
		} else {
			int age = util.calculateAge(customer.getCustDob());
			if (age >= 18) {
				customer.setCustAge(age);
			} else {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setCustAge(-1);
				newCus.setStatusMessage(AGE_INSUFF);
				return newCus;
			}
		}
		return customer;
	}

}
