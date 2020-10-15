/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.dao.service.RewardPointsDAOService;
import com.postang.domain.RewardPoints;
import com.postang.service.RewardPointsService;

/**
 * @author Subrahmanya Vijay
 *
 */
@Service
public class RewardPointsServiceImpl implements RewardPointsService {

	@Autowired
	RewardPointsDAOService rewardPointsDAOService;

	@Override
	public List<RewardPoints> getRewardPointsByUserId(long userId) {
		return rewardPointsDAOService.getRewardPointsByUserId(userId);
	}

	@Override
	public List<RewardPoints> getRewardPointsByUserName(String userName) {
		return rewardPointsDAOService.getRewardPointsByUserName(userName);
	}

}
