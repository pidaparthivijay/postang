/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.RewardPoints;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsService {

	List<RewardPoints> getRewardPointsByUserId(long userId);

	List<RewardPoints> getRewardPointsByUserName(String userName);

}
