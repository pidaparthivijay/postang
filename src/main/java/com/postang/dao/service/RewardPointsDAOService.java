/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.RewardPoints;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsDAOService {

	List<RewardPoints> getRewardPointsByUserId(long userId);

	RewardPoints saveRewardPoints(RewardPoints rewardPoints);

}
