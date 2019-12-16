package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Customer;
import com.postang.service.common.CustomerService;
import com.postang.util.MailUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class HomeController {

	@Autowired
	CustomerService customerService;
	
	MailUtil mailUtil = new MailUtil();

	@RequestMapping(value = "/brw/registerCustomer", method = { RequestMethod.POST })
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

	/*
	 * @RequestMapping(value = "/brw/findAll", method = { RequestMethod.GET
	 * ,RequestMethod.POST }) public String findAll() { String
	 * customerJsonString=""; log.info("registerCustomer starts..."); try {
	 * Iterable<Customer> customerList = customerService.findAll();
	 * customerJsonString=new ObjectMapper().writeValueAsString(customerList); }
	 * catch (Exception ex) { log.info("Exception is: " + ex); } return
	 * customerJsonString; }
	 * 
	 * @RequestMapping(value = "/brw/findById", method = { RequestMethod.GET
	 * ,RequestMethod.POST }) public String findById(@RequestParam("id") long id) {
	 * String customerJsonString=""; log.info("registerCustomer starts..."); try {
	 * customerJsonString=customerService.findById(id).toString(); } catch
	 * (Exception ex) { log.info("Exception is: " + ex); } return
	 * customerJsonString; }
	 * 
	 * @RequestMapping(value = "/brw/findByUserName", method = { RequestMethod.GET
	 * ,RequestMethod.POST }) public String findByUserName(@RequestParam("userName")
	 * String userName) { String customerJsonString="";
	 * log.info("registerCustomer starts..."); try {
	 * customerJsonString=customerService.findByUserName(userName).toString(); }
	 * catch (Exception ex) { log.info("Exception is: " + ex); } return
	 * customerJsonString; }
	 */
}
