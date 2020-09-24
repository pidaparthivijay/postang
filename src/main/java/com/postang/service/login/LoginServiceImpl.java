package com.postang.service.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.model.Constants;
import com.postang.model.OneTimePassword;
import com.postang.model.User;
import com.postang.repo.OneTimePassRepo;
import com.postang.repo.UserRepository;
import com.postang.service.common.CustomerService;
import com.postang.util.MailUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Service
@Log4j2
public class LoginServiceImpl implements Constants,LoginService {
	@Autowired
	UserRepository userRepository;
 	@Autowired
	CustomerService customerService;
	@Autowired
	OneTimePassRepo oneTimePassRepo;
	
	MailUtil mailUtil = new MailUtil();
	
	Util util = new Util();

	@Override
	public User validateUserDetails(User user) {
		log.info("validateUserDetails starts: ");
		try {
			User loginUser = userRepository.findByUserName(user.getUserName());
			if (loginUser == null) {
				return null;
			} else {
				boolean detailsValidated = util.validateLogin(user, loginUser);
				if (!detailsValidated) {
					return null;
				} else {
					return loginUser;
				}
			}
		} catch (Exception e) {
			log.info("Exception in validateUserDetails: " + e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String restoreAccount(User user) {
		OneTimePassword oneTimePassword= util.generateOTP(user);		
		//String toList=customerService.getCustomerByUserName(user.getUserName()).getCustEmail();
		String mailStatus=mailUtil.sendOTPMail(user, oneTimePassword.getOtpValue());
		oneTimePassRepo.save(oneTimePassword);
		return mailStatus;
	}

	@Override
	public String validateOtp(OneTimePassword oneTimePassword) {
		List<OneTimePassword> savedOTPList = oneTimePassRepo.findByUserName(oneTimePassword.getUserName());
		for(OneTimePassword otp:savedOTPList) {			
			if(otp.isValid()) {
				log.info("inside otp valid");
				log.info(Integer.parseInt(oneTimePassword.getOtpValue()));
				log.info(Integer.parseInt(otp.getOtpValue()));
				if(Integer.parseInt(oneTimePassword.getOtpValue()) != Integer.parseInt(otp.getOtpValue())) {
					log.info("inequal");
					return INVALID_OTP;
				}
				else if(Integer.parseInt(oneTimePassword.getOtpValue()) == Integer.parseInt(otp.getOtpValue())) {
					log.info("Equal");
					return VALID_OTP;
				}	
			}
			
		}		
		return null;
	}

	@Override
	public String resetPwd(User user) {
		try {
			User savedUser=this.getUserDetailsByUserName(user.getUserName());
			if (savedUser != null) {
				savedUser.setPassword(user.getPassword());
				User newUser = userRepository.save(savedUser);
				if (newUser != null) {
					return PWD_RESET_SUCCESS;
				} else {
					return PWD_RESET_FAILURE;
				}
			} else {
				log.info("There is no user with given userName: "+user.getUserName());
				return NO_USER_WITH_GIVEN_NAME;
			}
		} catch (Exception e) {
			log.info("Exception occured in resetPwd: "+e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUserDetailsByUserName(String userName) {
		User loginUser = null;
		log.info("getUserDetailsByUserName starts: ");
		try {
			loginUser = userRepository.findByUserName(userName);
		}catch(Exception e) {
			log.info("Exception in getUserDetails: "+e);
			log.error(e);
		}
		return loginUser;
	}

}
