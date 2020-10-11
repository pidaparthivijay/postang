/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import com.postang.model.RewardPoints;
import com.postang.model.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsService {

	RewardPoints allocateRewardPoints(User user, String reasonCode);

	List<RewardPoints> getRewardPointsByUserId(long userId);

}
