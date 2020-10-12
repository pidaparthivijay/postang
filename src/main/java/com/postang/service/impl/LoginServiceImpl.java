package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.postang.constants.Constants;
import com.postang.domain.OneTimePassword;
import com.postang.domain.User;
import com.postang.model.MailDTO;
import com.postang.repo.OneTimePassRepo;
import com.postang.repo.UserRepository;
import com.postang.service.CustomerService;
import com.postang.service.LoginService;
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
	CustomerService customerService;
 	MailUtil mailUtil = new MailUtil();
	@Autowired
	OneTimePassRepo oneTimePassRepo;
	
	@Autowired
	UserRepository userRepository;
	
	Util util = new Util();

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

	@Override
	public String requestOTPMail(User user) {
		OneTimePassword oneTimePassword = util.generateOTP(user);
		MailDTO mailDTO = new MailDTO();
		mailDTO.setUser(user);
		mailDTO.setOneTimePassword(oneTimePassword.getOtpValue());
		mailDTO.setTemplateName(TEMPLATE_OTP_MAIL);
		String mailStatus = mailUtil.triggerMail(mailDTO);
		oneTimePassRepo.save(oneTimePassword);
		return mailStatus;
	}

	@Override
	public String resetPwd(User user) {
		try {
			String newPwd = user.getPassword();
			User existingDetails = this.getUserDetailsByUserName(user.getUserName());
			if (existingDetails != null) {
				existingDetails.setPassword(user.getPassword());
				User newDetails = userRepository.save(existingDetails);
				return newPwd.equalsIgnoreCase(newDetails.getPassword()) ? PWD_RESET_SUCCESS : PWD_RESET_FAILURE;
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
	public String validateOtp(OneTimePassword oneTimePassword) {
		log.info("validateOtp starts with: " + oneTimePassword);
		List<OneTimePassword> savedOTPList = oneTimePassRepo.findByUserName(oneTimePassword.getUserName());
		if (!CollectionUtils.isEmpty(savedOTPList)) {
			for (OneTimePassword otp : savedOTPList) {
				if (otp.isValid()) {
					if (Integer.parseInt(oneTimePassword.getOtpValue()) != Integer.parseInt(otp.getOtpValue())) {
						log.info("inequal");
						return INVALID_OTP;
					} else if (Integer.parseInt(oneTimePassword.getOtpValue()) == Integer.parseInt(otp.getOtpValue())) {
						log.info("Equal");
						return VALID_OTP;
					}
				} else {
					return NO_OTP_GIVEN_USERNAME;
				}
			}
		} else {
			return NO_OTP_GIVEN_USERNAME;
		}
		return null;
	}

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

}