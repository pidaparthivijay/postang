package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.OneTimePassword;
import com.postang.domain.User;
import com.postang.model.RequestDTO;
import com.postang.service.CustomerService;
import com.postang.service.EmployeeService;
import com.postang.service.LoginService;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = RequestMappings.BRW)
public class LoginController implements RequestMappings, Constants {

	Util util = new Util();

	@Autowired
	LoginService loginService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	CustomerService customerService;

	@PostMapping(value = LOGIN)
	public String loginMethod(@RequestBody User user) {
		User loginUser = null;
		try {
			loginUser = loginService.validateUserDetails(user);
			if (loginUser != null) {
				if (CUSTOMER.equalsIgnoreCase(loginUser.getUserType())) {
					Customer customer = customerService.getCustomerByUserName(loginUser.getUserName());
					customer.setUserId(loginUser.getUserId());
					customer.setStatusMessage(CUST_LOG_SUCCESS);
					return new ObjectMapper().writeValueAsString(customer);
				} else {
					Employee employee = employeeService.getEmployeeDetails(user.getUserName());
					employee.setUserId(loginUser.getUserId());
					if (EMPLOYEE.equalsIgnoreCase(loginUser.getUserType())) {
						employee.setStatusMessage(EMP_LOG_SUCCESS);
					} else if (ADMIN.equalsIgnoreCase(loginUser.getUserType())) {
						employee.setStatusMessage(ADM_LOG_SUCCESS);
					}
					return new ObjectMapper().writeValueAsString(employee);
				}
			} else {
				user.setStatusMessage(INVALID);
				return new ObjectMapper().writeValueAsString(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = OTP_REQUEST)
	public String requestOTP(@RequestBody User user) {
		try {
			String statusMsg = loginService.requestOTPMail(user);
			if (!StringUtils.isEmpty(statusMsg)) {
				return TRUE;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = OTP_SUBMIT)
	public String submitOtp(@RequestBody OneTimePassword oneTimePassword) {
		try {
			String otpStatus = loginService.validateOtp(oneTimePassword);
			if (VALID_OTP.equalsIgnoreCase(otpStatus)) {
				User user = new User();
				user.setUserName(oneTimePassword.getUserName());
				user = loginService.getUserDetailsByUserName(user.getUserName());
				if (CUSTOMER.equals(user.getUserType())) {
					Customer customer = customerService.getCustomerByUserName(user.getUserName());
					customer.setActionStatus(true);
					customer.setStatusMessage(VALID_OTP);
					return new ObjectMapper().writeValueAsString(customer);
				} else if (EMPLOYEE.equals(user.getUserType())) {
					Employee employee = employeeService.getEmployeeDetails(user.getUserName());
					employee.setActionStatus(true);
					employee.setStatusMessage(VALID_OTP);
					return new ObjectMapper().writeValueAsString(employee);
				}
			} else
				return new ObjectMapper().writeValueAsString(otpStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = RESET_PWD)
	public RequestDTO resetPwd(@RequestBody User user) {
		RequestDTO requestDTO = new RequestDTO();
		String statusMsg = "";
		try {
			statusMsg = loginService.resetPwd(user);
			requestDTO.setActionStatus(statusMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestDTO;
	}
}
