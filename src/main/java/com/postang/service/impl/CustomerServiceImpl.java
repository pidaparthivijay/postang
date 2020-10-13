/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.domain.Customer;
import com.postang.domain.User;
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
	CommonDAOService commonDAOService;

	Util util = new Util();

	@Override
	public List<Customer> findAll() {
		return commonDAOService.findAll();
	}

	@Override
	public Customer getCustomerByUserName(String userName) {
		return commonDAOService.getCustomerByUserName(userName);
	}

	@Override
	public Customer getCustomerDetails(Customer customer) {

		Customer existingCustomer = commonDAOService.getCustomerByUserName(customer.getUserName());
		User existingUser = commonDAOService.findUserByUserName(customer.getUserName());
		if (existingCustomer != null && existingUser != null) {
			existingCustomer.setCustPass(existingUser.getPassword());
			return existingCustomer;
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
			User newUser = commonDAOService.saveUser(user);
			if (newUser == null) {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setStatusMessage(USER_INVALID);
				return newCus;
			}
			return commonDAOService.saveCustomer(customer);
		} catch (Exception ex) {
			log.info("Exception in saveCustomer: " + ex);
			ex.printStackTrace();
		}
		return null;

	}

	private Customer validate(Customer customer) {
		Customer existingCustomer = commonDAOService.getCustomerByUserName(customer.getUserName());
		if (existingCustomer != null) {
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
