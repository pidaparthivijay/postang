package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.RewardPoints;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsRepository extends CrudRepository<RewardPoints, Long> {

	List<RewardPoints> findByUserId(long userId);
}
