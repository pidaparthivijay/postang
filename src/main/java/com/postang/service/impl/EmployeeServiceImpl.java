/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.domain.Employee;
import com.postang.domain.User;
import com.postang.model.MailDTO;
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

	MailUtil mailUtil = new MailUtil();

	@Autowired
	CommonDAOService commonDAOService;

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
			User newUser = commonDAOService.saveUser(user);
			if (newUser == null) {
				Employee newEmp = new Employee();
				newEmp.setEmpId(-1L);
				newEmp.setActionStatus(false);
				newEmp.setStatusMessage(USER_INVALID);
				return newEmp;
			}
			Employee emp = commonDAOService.saveEmployee(employee);
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
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return commonDAOService.getAllEmployees();
	}

	@Override
	public Employee getEmployeeDetails(String userName) {
		return commonDAOService.getEmployeeDetails(userName);
	}

	private Employee validate(Employee employee) {
		Employee existingEmployee = commonDAOService.getEmployeeDetails(employee.getUserName());
		if (existingEmployee != null) {
			Employee newEmp = new Employee();
			newEmp.setEmpId(-1L);
			newEmp.setActionStatus(false);
			newEmp.setStatusMessage(USERNAME_TAKEN);
			return newEmp;
		}
		return employee;
	}

	@Override
	public Employee updateEmployee(Employee employee) {
		Employee existingEmployee = commonDAOService.getEmployeeDetails(employee.getUserName());
		BeanUtils.copyProperties(employee, existingEmployee);
		User user = commonDAOService.findUserByUserName(employee.getUserName());
		user.setName(employee.getEmpName());
		if (!StringUtils.isEmpty(employee.getEmpPass())) {
			user.setPassword(employee.getEmpPass());
		}
		commonDAOService.saveUser(user);
		return commonDAOService.saveEmployee(existingEmployee);
	}

}
