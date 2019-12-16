package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class LoginController {

	Util util = new Util();

	@Autowired
	LoginService loginService;

	@Autowired
	CustomerService customerService;

	@RequestMapping(value = "/brw/login", method = { RequestMethod.POST })
	public String loginMethod(@RequestBody User user) {
		User loginUser = null;
		log.info("loginMethod starts: ");
		try {
			loginUser = loginService.validateUserDetails(user);
			if (loginUser != null) {
				if (Constants.CUSTOMER.equalsIgnoreCase(loginUser.getUserType())) {
					Customer customer = customerService.getCustomerByUserName(loginUser.getUserName());
					customer.setStatusMessage(Constants.CUST_LOG_SUCCESS);
					return new ObjectMapper().writeValueAsString(customer);
				} else if (Constants.EMPLOYEE.equalsIgnoreCase(loginUser.getUserType())) {
					Employee employee = customerService.getEmployeeByUserName(user.getUserName());
					employee.setStatusMessage(Constants.EMP_LOG_SUCCESS); 
					return new ObjectMapper().writeValueAsString(employee);					 
				} else if (Constants.ADMIN.equalsIgnoreCase(loginUser.getUserType())) {					
					Employee employee = customerService.getEmployeeByUserName(user.getUserName());
					employee.setStatusMessage(Constants.ADM_LOG_SUCCESS); 
					return new ObjectMapper().writeValueAsString(employee);					 
				}
			} else {
				user.setUserType(Constants.INVALID);
				return new ObjectMapper().writeValueAsString(user);
			}
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
			ex.printStackTrace();
		}
		log.info("loginMethod ends: ");
		return null;
	}

	@RequestMapping(value = "/brw/requestOTP", method = { RequestMethod.POST })
	public String restorePwd(@RequestBody User user) {
		log.info("restorePwd starts: ");
		try {
			String statusMsg = loginService.restoreAccount(user);
			if (statusMsg.equals(Constants.MAIL_SUCCESS)) {
				return Constants.TRUE;
			}
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		log.info("restorePwd ends: ");
		return null;
	}

	@RequestMapping(value = "/brw/submitOtp", method = { RequestMethod.POST })
	public String submitOtp(@RequestBody OneTimePassword oneTimePassword) {
		try {
			String otpStatus = loginService.validateOtp(oneTimePassword);
			if (Constants.VALID_OTP.equalsIgnoreCase(otpStatus)) {
				User user = new User();
				user.setUserName(oneTimePassword.getUserName());
				user = loginService.getUserDetailsByUserName(user.getUserName());
				if (Constants.CUSTOMER.equals(user.getUserType())) {
					Customer customer = customerService.getCustomerByUserName(user.getUserName());
					customer.setActionStatus(true);
					customer.setStatusMessage(Constants.VALID_OTP);
					return new ObjectMapper().writeValueAsString(customer);
				} else if (Constants.EMPLOYEE.equals(user.getUserType())) {
					/*
					 * Customer customer=commonService.getCustomerByUserName(user.getUserName());
					 * customer.setActionStatus(true);
					 * customer.setStatusMessage(Constants.VALID_OTP); return new
					 * ObjectMapper().writeValueAsString(customer);
					 */
				}
			} else
				return new ObjectMapper().writeValueAsString(otpStatus);
		} catch (Exception e) {
			log.info("Exception is: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/brw/resetPwd", method = { RequestMethod.POST })
	public String resetPwd(@RequestBody User user) {
		String statusMsg = "";
		try {
			statusMsg = loginService.resetPwd(user);
		} catch (Exception e) {
			log.info("Exception is: " + e);
			e.printStackTrace();
		}
		return statusMsg;
	}
}
