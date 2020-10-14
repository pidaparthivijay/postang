package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.RewardPoints;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface RewardPointsRepository extends JpaRepository<RewardPoints, Long> {

	List<RewardPoints> findByUserId(long userId);

	List<RewardPoints> findByUserName(String userName);
}
