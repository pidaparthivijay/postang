/**
 * 
 */
package com.postang.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.Customer;
import com.postang.domain.RewardPoints;
import com.postang.model.MailDTO;
import com.postang.model.RequestDTO;
import com.postang.service.CustomerService;
import com.postang.service.RewardPointsService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@Log4j2
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class CustomerController implements RequestMappings, Constants {

	@Autowired
	CustomerService customerService;

	MailUtil mailUtil = new MailUtil();

	@Autowired
	RewardPointsService rewardPointsService;

	/*************************
	 *** Customer Operations***
	 *************************/

	@PostMapping(value = CUSTOMER_REGISTER)
	public RequestDTO registerCustomer(@RequestBody RequestDTO requestDTO) {
		Customer customer = requestDTO.getCustomer();
		try {
			customer = customerService.saveCustomer(customer);

			if (customer.getCustId() > 0) {
				customer.setActionStatus(true);
				customer.setStatusMessage("");
				MailDTO mailDTO = new MailDTO();
				mailDTO.setCustomer(customer);
				mailDTO.setTemplateName(TEMPLATE_SIGN_UP_MAIL);
				String mailStatus = mailUtil.triggerMail(mailDTO);
				log.info(mailStatus);

			} else {
				requestDTO.setStatusMessage(customer.getStatusMessage());
			}
			requestDTO.setActionStatus(customer.getCustId() > 0 ? CUST_REG_SXS : CUST_REG_FAIL);
			requestDTO.setCustomer(customer);

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = CUSTOMER_DETAILS)
	public RequestDTO getCustomerDetails(@RequestBody RequestDTO requestDTO) {
		Customer customer = requestDTO.getCustomer();
		try {
			customer = customerService.getCustomerDetails(customer);
			requestDTO.setCustomer(customer);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = UPDATE_PROFILE_CUST)
	public RequestDTO updateCustomerDetails(@RequestBody RequestDTO requestDTO) {
		Customer customer = requestDTO.getCustomer();
		try {
			customer = customerService.updateCustomerDetails(customer);
			requestDTO.setCustomer(customer);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}
	
	/******************************
	 *** Reward Points Operations***
	 ******************************/

	@PostMapping(value = CUSTOMER_VIEW_RWD_POINTS)
	public RequestDTO viewRewardPoints(@RequestBody RequestDTO requestDTO) {
		String userName = requestDTO.getUserName();
		try {
			List<RewardPoints> rewardPointsList = rewardPointsService.getRewardPointsByUserName(userName);
			if (CollectionUtils.isEmpty(rewardPointsList)) {
				requestDTO.setActionStatus(NO_REWARDS_FOUND);
				return requestDTO;
			} else {
				long totalCount = 0l;
				for (RewardPoints rewardPoints : rewardPointsList) {
					if (!EXPIRED.equals(rewardPoints.getPointsStatus())) {
						totalCount += rewardPoints.getPointsEarned();
					}
				}
				RewardPoints rewardPoints = new RewardPoints();
				BeanUtils.copyProperties(rewardPointsList.get(0), rewardPoints);
				rewardPoints.setPointsTransactionName(TOTAL_POINTS);
				rewardPoints.setPointsEarned(totalCount);
				rewardPoints.setPointsStatus(null);
				rewardPoints.setPointsExpiryDate(null);
				rewardPoints.setPointsEarnedDate(null);
				rewardPointsList.add(rewardPoints);
				requestDTO.setRewardPointsList(
						StreamSupport.stream(rewardPointsList.spliterator(), false).collect(Collectors.toList()));
			}

		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			ex.printStackTrace();
		}

		return requestDTO;
	}


}
