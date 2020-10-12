package com.postang.service;

import com.postang.domain.OneTimePassword;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface LoginService {

	public User getUserDetailsByUserName(String userName);

	public String requestOTPMail(User user);

	public String resetPwd(User user);

	public String validateOtp(OneTimePassword oneTimePassword);

	public User validateUserDetails(User user);

}
