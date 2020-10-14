/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.dao.service.RewardPointsDAOService;
import com.postang.domain.RewardPoints;
import com.postang.repo.CustomerRepository;
import com.postang.repo.RewardPointsRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Repository
public class RewardPointsDAOServiceImpl implements RewardPointsDAOService {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	RewardPointsRepository rewardPointsRepo;

	Util util = new Util();

	@Override
	public RewardPoints saveRewardPoints(RewardPoints rewardPoints) {
		return rewardPointsRepo.save(rewardPoints);
	}

	@Override
	public List<RewardPoints> getRewardPointsByUserId(long userId) {
		return rewardPointsRepo.findByUserId(userId);
	}

	@Override
	public List<RewardPoints> getRewardPointsByUserName(String userName) {
		return rewardPointsRepo.findByUserName(userName);
	}

}
