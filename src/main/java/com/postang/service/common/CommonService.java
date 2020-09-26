/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.Employee;
import com.postang.model.RewardPoints;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface CommonService {
	public RewardPoints allocateRewardPoints(User user, String reasonCode);

	public Employee getEmployeeByUserName(String userName);

	public List<RewardPoints> getRewardPointsByUserId(long userId);

}
