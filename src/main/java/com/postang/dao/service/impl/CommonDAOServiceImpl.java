/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.OneTimePassword;
import com.postang.domain.User;
import com.postang.repo.CustomerRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.OneTimePassRepo;
import com.postang.repo.UserRepository;

/**
 * @author Subrahmanya Vijay
 *
 */
@Repository
public class CommonDAOServiceImpl implements CommonDAOService, Constants {

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	OneTimePassRepo oneTimePassRepo;

	@Override
	public List<Customer> findAll() {
		return custRepo.findAll();
	}

	@Override
	public Customer getCustomerByUserName(String userName) {
		return custRepo.findByUserName(userName);
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		return custRepo.save(customer);

	}

	@Override
	public Employee saveEmployee(Employee employee) {
		return empRepo.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return empRepo.findAll();
	}

	@Override
	public Employee getEmployeeDetails(String userName) {
		return empRepo.findByUserName(userName);
	}

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public User findUserByUserName(String userName) {
		return userRepo.findByUserName(userName);
	}

	@Override
	public User findUserByUserId(int userId) {
		return userRepo.findByUserId(userId);
	}

	@Override
	public User findUserByUserMail(String userMail) {
		return userRepo.findByUserMail(userMail);
	}

	@Override
	public void saveOTP(OneTimePassword oneTimePassword) {
		oneTimePassRepo.save(oneTimePassword);

	}

	@Override
	public List<OneTimePassword> findOTPByUserName(String userName) {
		return oneTimePassRepo.findByUserName(userName);
	}

}
