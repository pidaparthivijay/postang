package com.postang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.BillPendingRequest;
import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.service.common.CustomerService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class HomeController implements Constants{

	@Autowired
	CustomerService customerService;
	
	MailUtil mailUtil = new MailUtil();

	@PostMapping(value = "/brw/registerCustomer")
	public String registerCustomer(@RequestBody Customer customer) {
		String customerJsonString = "";
		log.info("registerCustomer starts..." + customer);
		try {
			customer = customerService.saveCustomer(customer);

			if (customer.getCustId() > 0) {
				customer.setActionStatus(true);
				customer.setStatusMessage("");
				String mailStatus = mailUtil.sendSignUpMail(customer);
				log.info(mailStatus);
			}
			customerJsonString = new ObjectMapper().writeValueAsString(customer);
			log.info("registerCustomer ends..." + customer.getCustId());
		} catch (Exception ex) {
			log.info("Exception is: " + ex);
		}
		return customerJsonString;
	}
	
	@PostMapping(value = "/brw/getPendingBillRequests")
	public String getPendingBillRequests(@RequestBody String custEmail) {
		String pendingBillJson = "";
		log.info("getPendingBillRequests starts..." + custEmail);
		try {
			List<BillPendingRequest> billPendingRequests= customerService.getPendingBillRequests(custEmail);
			BillPendingRequest totalBillPendingRequest= new BillPendingRequest(); 
			double customerBill=customerService.generateBill(custEmail);
			totalBillPendingRequest.setBillAmount(customerBill);
			totalBillPendingRequest.setTypeOfRequest(TOTAL_BILL_AMOUNT);
			billPendingRequests.add(totalBillPendingRequest);
			pendingBillJson= new ObjectMapper().writeValueAsString(billPendingRequests);
			log.info(pendingBillJson);
		} catch (Exception e) {
			log.error("Exception occured in getPendingBillRequests: " + e);
			e.printStackTrace();

		}
		return pendingBillJson;
	}
	
	
}
