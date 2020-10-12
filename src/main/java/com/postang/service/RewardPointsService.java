/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.RewardPoints;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsService {

	RewardPoints allocateRewardPoints(User user, String reasonCode);

	List<RewardPoints> getRewardPointsByUserId(long userId);

}
