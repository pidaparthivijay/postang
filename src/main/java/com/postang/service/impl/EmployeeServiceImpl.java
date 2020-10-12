/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.domain.Employee;
import com.postang.domain.User;
import com.postang.model.MailDTO;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.UserRepository;
import com.postang.service.EmployeeService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService, Constants {

	@Autowired
	EmployeeRepository employeeRepository;

	MailUtil mailUtil = new MailUtil();

	@Autowired
	UserRepository userRepository;

	Util util = new Util();

	@Override
	public Employee createEmployee(Employee employee) {

		User user = new User();
		try {

			Employee validatedEmployee = this.validate(employee);
			if (!StringUtils.isEmpty(validatedEmployee.getStatusMessage())) {
				return validatedEmployee;
			}
			user = util.generateUserFromEmployee(employee);
			User newUser = userRepository.save(user);
			if (newUser == null) {
				Employee newEmp = new Employee();
				newEmp.setEmpId(-1L);
				newEmp.setActionStatus(false);
				newEmp.setStatusMessage(USER_INVALID);
				return newEmp;
			}
			Employee emp = employeeRepository.save(employee);
			if (emp.getEmpId() > 0) {
				emp.setActionStatus(true);
				emp.setStatusMessage("");
				emp.setEmpPass(user.getPassword());
				MailDTO mailDTO = new MailDTO();
				mailDTO.setEmployee(emp);
				mailDTO.setTemplateName(TEMPLATE_CUST_SIGN_UP_MAIL);
				String mailStatus = mailUtil.triggerMail(mailDTO);
				log.info(mailStatus);
			}
			return emp;
		} catch (Exception ex) {
			log.info("Exception in createEmployee: " + ex);
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList = null;
		log.info("getEmployeeByUserName starts with userName: " + userName);
		try {
			empList = employeeRepository.findByUserName(userName);
			if (!empList.isEmpty()) {
				return empList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("Exception in getEmployeeByUserName :" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Employee getEmployeeDetails(String userName) {
		return employeeRepository.findByUserName(userName).get(0);
	}

	private Employee validate(Employee employee) {
		List<Employee> customerList = null;
		customerList = employeeRepository.findByUserName(employee.getUserName());
		if (!customerList.isEmpty()) {
			Employee newEmp = new Employee();
			newEmp.setEmpId(-1L);
			newEmp.setActionStatus(false);
			newEmp.setStatusMessage(USERNAME_TAKEN);
			return newEmp;
		}
		return employee;
	}

}
