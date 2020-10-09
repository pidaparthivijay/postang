package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.OneTimePassword;
import com.postang.model.RequestDTO;
import com.postang.model.User;
import com.postang.service.common.AdminService;
import com.postang.service.common.CustomerService;
import com.postang.service.login.LoginService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(value = RequestMappings.BRW)
public class LoginController implements RequestMappings, Constants {

	Util util = new Util();

	@Autowired
	LoginService loginService;

	@Autowired
	AdminService adminService;

	@Autowired
	CustomerService customerService;

	@PostMapping(value = LOGIN)
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
				user.setStatusMessage(INVALID);
				return new ObjectMapper().writeValueAsString(user);
			}
		} catch (Exception ex) {
			log.error("Exception occured in loginMethod: " + ex);
			ex.printStackTrace();
		}
		log.info("loginMethod ends: ");
		return null;
	}

	@PostMapping(value = OTP_REQUEST)
	public String requestOTP(@RequestBody User user) {
		log.info("requestOTP starts: ");
		try {
			String statusMsg = loginService.requestOTPMail(user);
			if (statusMsg.equals(MAIL_SUCCESS)) {
				return TRUE;
			}
		} catch (Exception ex) {
			log.error("Exception occured in restorePwd: " + ex);
		}
		log.info("requestOTP ends: ");
		return null;
	}

	@PostMapping(value = OTP_SUBMIT)
	public String submitOtp(@RequestBody OneTimePassword oneTimePassword) {
		try {
			log.info("submitOtp starts: ");
			String otpStatus = loginService.validateOtp(oneTimePassword);
			log.info("otpStatus: " + otpStatus);
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
					Employee employee = adminService.getEmployeeDetails(user.getUserName());
					employee.setActionStatus(true);
					employee.setStatusMessage(VALID_OTP);
					return new ObjectMapper().writeValueAsString(employee);
				}
			} else
				return new ObjectMapper().writeValueAsString(otpStatus);
		} catch (Exception e) {
			log.error("Exception occured in submitOtp: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = EMPLOYEE_VIEW_DETAILS)
	public RequestDTO viewEmployeeDetails(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("viewEmployeeDetails starts...");
		try {
			Employee emp = adminService.getEmployeeDetails(employee.getUserName());
			requestDTO.setEmployee(emp);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewEmployeeDetails : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = EMPLOYEE_UPDATE)
	public RequestDTO udpateEmployee(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("udpateEmployee starts...");
		try {
			Employee emp = adminService.createEmployee(employee);
			requestDTO.setEmployee(emp);
			requestDTO.setActionStatus((emp != null && emp.getEmpId() > 0) ? EMP_UPDATE_SXS : EMP_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in udpateEmployee : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = RESET_PWD)
	public RequestDTO resetPwd(@RequestBody User user) {
		RequestDTO requestDTO = new RequestDTO();
		String statusMsg = "";
		try {
			statusMsg = loginService.resetPwd(user);
			requestDTO.setActionStatus(statusMsg);
		} catch (Exception e) {
			log.error("Exception occured in resetPwd: " + e);
			e.printStackTrace();
		}
		return requestDTO;
	}
}
