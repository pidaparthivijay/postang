package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.OneTimePassword;
import com.postang.model.User;
import com.postang.service.common.CustomerService;
import com.postang.service.login.LoginService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class LoginController implements Constants{

	Util util = new Util();

	@Autowired
	LoginService loginService;

	@Autowired
	CustomerService customerService;

	@PostMapping(value = "/brw/login")
	public String loginMethod(@RequestBody User user) {
		User loginUser = null;
		log.info("loginMethod starts: ");
		try {
			loginUser = loginService.validateUserDetails(user);
			if (loginUser != null) {
				if (CUSTOMER.equalsIgnoreCase(loginUser.getUserType())) {
					Customer customer = customerService.getCustomerByUserName(loginUser.getUserName());
					customer.setUserId(loginUser.getUserId());
					customer.setStatusMessage(CUST_LOG_SUCCESS);
					return new ObjectMapper().writeValueAsString(customer);
				} else {
					Employee employee = customerService.getEmployeeByUserName(user.getUserName());
					employee.setUserId(loginUser.getUserId());
					if (EMPLOYEE.equalsIgnoreCase(loginUser.getUserType())) {
						employee.setStatusMessage(EMP_LOG_SUCCESS); 
					} else if (ADMIN.equalsIgnoreCase(loginUser.getUserType())) {					
						employee.setStatusMessage(ADM_LOG_SUCCESS); 
				}
					return new ObjectMapper().writeValueAsString(employee);
				}
			} else {
				user.setUserType(INVALID);
				return new ObjectMapper().writeValueAsString(user);
			}
		} catch (Exception ex) {
			log.error("Exception occured in loginMethod: " + ex);
			ex.printStackTrace();
		}
		log.info("loginMethod ends: ");
		return null;
	}

	@PostMapping(value = "/brw/requestOTP")
	public String restorePwd(@RequestBody User user) {
		log.info("restorePwd starts: ");
		try {
			String statusMsg = loginService.restoreAccount(user);
			if (statusMsg.equals(MAIL_SUCCESS)) {
				return TRUE;
			}
		} catch (Exception ex) {
			log.error("Exception occured in restorePwd: " + ex);
		}
		log.info("restorePwd ends: ");
		return null;
	}

	@PostMapping(value = "/brw/submitOtp")
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
					/*
					 * Customer customer=commonService.getCustomerByUserName(user.getUserName());
					 * customer.setActionStatus(true);
					 * customer.setStatusMessage(VALID_OTP); return new
					 * ObjectMapper().writeValueAsString(customer);
					 */
				}
			} else
				return new ObjectMapper().writeValueAsString(otpStatus);
		} catch (Exception e) {
			log.error("Exception occured in submitOtp: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = "/brw/resetPwd")
	public String resetPwd(@RequestBody User user) {
		String statusMsg = "";
		try {
			statusMsg = loginService.resetPwd(user);
		} catch (Exception e) {
			log.error("Exception occured in resetPwd: " + e);
			e.printStackTrace();
		}
		return statusMsg;
	}
}
