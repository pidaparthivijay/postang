/**
 * 
 */
package com.postang.service.common;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.RewardPoints;
import com.postang.model.User;
import com.postang.repo.CustomerRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.RewardPointsRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	RewardPointsRepository rewardPointsRepo;

	Util util = new Util();

	@Override
	public RewardPoints allocateRewardPoints(User user, String reasonCode) {

		RewardPoints rewardPoints = new RewardPoints();
		rewardPoints.setPointsTransactionName(reasonCode);
		rewardPoints.setPointsEarned(util.getPointsForTrxn(reasonCode));
		rewardPoints.setPointsEarnedDate(new Date());
		rewardPoints.setPointsExpiryDate(util.getOneYearFromToday());
		rewardPoints.setUserId(user.getUserId());
		List<Customer> customerList = customerRepo.findByUserName(user.getUserName());
		rewardPoints.setCustId(customerList.get(0).getCustId());
		return rewardPointsRepo.save(rewardPoints);
	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList = null;
		log.info("getEmployeeByUserName starts with userName: " + userName);
		try {
			empList = empRepo.findByUserName(userName);
			if (!empList.isEmpty()) {
				return empList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("Exception in getEmployeeByUserName :" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<RewardPoints> getRewardPointsByUserId(long userId) {
		return rewardPointsRepo.findByUserId(userId);
	}

}
