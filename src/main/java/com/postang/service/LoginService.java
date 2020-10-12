package com.postang.service;

import com.postang.domain.OneTimePassword;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface LoginService {

	User getUserDetailsByUserName(String userName);

	String requestOTPMail(User user);

	String resetPwd(User user);

	String validateOtp(OneTimePassword oneTimePassword);

	User validateUserDetails(User user);

}
