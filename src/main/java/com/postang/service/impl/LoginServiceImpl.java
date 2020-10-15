package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.postang.constants.Constants;
import com.postang.dao.service.CommonDAOService;
import com.postang.domain.OneTimePassword;
import com.postang.domain.User;
import com.postang.model.MailDTO;
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
public class LoginServiceImpl implements Constants, LoginService {

	MailUtil mailUtil = new MailUtil();

	@Autowired
	CommonDAOService commonDAOService;

	Util util = new Util();

	@Override
	public User getUserDetailsByUserName(String userName) {
		User loginUser = null;
		try {
			loginUser = commonDAOService.findUserByUserName(userName);
		} catch (Exception e) {
			e.printStackTrace();
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
		commonDAOService.saveOTP(oneTimePassword);
		return mailStatus;
	}

	@Override
	public String resetPwd(User user) {
		try {
			String newPwd = user.getPassword();
			User existingDetails = this.getUserDetailsByUserName(user.getUserName());
			if (existingDetails != null) {
				existingDetails.setPassword(user.getPassword());
				User newDetails = commonDAOService.saveUser(existingDetails);
				return newPwd.equalsIgnoreCase(newDetails.getPassword()) ? PWD_RESET_SUCCESS : PWD_RESET_FAILURE;
			} else {
				return NO_USER_WITH_GIVEN_NAME;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String validateOtp(OneTimePassword oneTimePassword) {
		List<OneTimePassword> savedOTPList = commonDAOService.findOTPByUserName(oneTimePassword.getUserName());
		if (!CollectionUtils.isEmpty(savedOTPList)) {
			for (OneTimePassword otp : savedOTPList) {
				if (otp.isValid()) {
					if (Integer.parseInt(oneTimePassword.getOtpValue()) != Integer.parseInt(otp.getOtpValue())) {
						log.info(INVALID_OTP);
						return INVALID_OTP;
					} else if (Integer.parseInt(oneTimePassword.getOtpValue()) == Integer.parseInt(otp.getOtpValue())) {
						 log.info(VALID_OTP);
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
		try {
			User loginUser = commonDAOService.findUserByUserName(user.getUserName());
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
			e.printStackTrace();
		}
		return null;
	}

}
