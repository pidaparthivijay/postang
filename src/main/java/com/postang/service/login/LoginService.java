package com.postang.service.login;

import com.postang.model.OneTimePassword;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface LoginService {

	public User getUserDetailsByUserName(String userName);

	public String requestOTPMail(User user);

	public String validateOtp(OneTimePassword oneTimePassword);

	public String resetPwd(User user);

	public User validateUserDetails(User user);

}
