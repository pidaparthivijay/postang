/**
 * 
 */
package com.postang.service.common;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.RoomRequest;
import com.postang.model.User;
import com.postang.repo.CustomerRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.UserRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	RoomRequestRepository roomReqRepo;

	Util util = new Util();
	
	@Override
	public Customer saveCustomer(Customer customer) {
		User user = new User();
		try {

			Customer validatedCustomer = this.validate(customer);
			if (!StringUtils.isEmpty(validatedCustomer.getStatusMessage())) {
				return validatedCustomer;
			}			
			user = this.generateUserFromCustomer(customer);
			User newUser = userRepo.save(user);
			if (newUser == null) {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setStatusMessage(Constants.USER_INVALID);
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
		if (customerList.size() > 0) {
			Customer newCus = new Customer();
			newCus.setCustId(-1L);
			newCus.setActionStatus(false);
			newCus.setStatusMessage(Constants.USERNAME_TAKEN);
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
				newCus.setStatusMessage(Constants.AGE_INSUFF);
				return newCus;
			}
		}
		return customer;
	}

	private User generateUserFromCustomer(Customer customer) {
		User genUser = new User();
		try {
			genUser.setCustName(customer.getCustName());
			genUser.setUserName(customer.getUserName());
			genUser.setPassword(customer.getCustPass());
			genUser.setUserMail(customer.getCustEmail());
			genUser.setUserMob(customer.getCustMob());
			genUser.setUserType(Constants.CUSTOMER);
		} catch (Exception e) {
			log.info("Exception while generating the user: " + e);
			e.printStackTrace();
		}
		return genUser;
	}

	@Override
	public Customer getCustomerByUserName(String userName) {
		List<Customer> custList= null;
		custList = custRepo.findByUserName(userName);
		if (custList != null && custList.size() > 0) {
			return custList.get(0);
		} else {
			return null;
		}
	}

	
	@Override
	public Optional<Customer> findById(long id) {		
		return custRepo.findById(id);
	}

	@Override
	public Iterable<Customer> findAll() {
		return custRepo.findAll();
	}

	@Override
	public Customer getCustomerDetails(Customer customer) {
		List<Customer> custList= null;
		User user=null; 
		custList = custRepo.findByUserName(customer.getUserName());
		user= userRepo.findByUserName(customer.getUserName());
		if (custList != null && custList.size() > 0) {
			custList.get(0).setCustPass(user.getPassword());
			return custList.get(0);
		} else {
			return null;
		}	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList= null;
		empList = empRepo.findByUserName(userName);
		if (empList != null && empList.size() > 0) {
			return empList.get(0);
		} else {
			return null;
		}	}

	@Override
	public RoomRequest requestRoom(RoomRequest roomRequest) {
		return roomReqRepo.save(roomRequest);
	}
}
