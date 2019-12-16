/**
 * 
 */
package com.postang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postang.model.Customer;
import com.postang.model.RoomRequest;
import com.postang.service.common.CustomerService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@RestController
@Log4j2
public class CustomerController {
	
	@Autowired
	CustomerService customerService;

	@RequestMapping(value = "/brw/getCustomerDetails", method = { RequestMethod.GET ,RequestMethod.POST })
	public String getCustomerDetails(@RequestBody Customer customer) {
		String customerJsonString="";
		Customer custDetails=null;
		log.info("getCustomerDetails starts...");
		try {
			custDetails=customerService.getCustomerDetails(customer);
			log.info("Infor is: "+customerService.getCustomerDetails(customer).toString());
			if(custDetails != null)
				customerJsonString=new ObjectMapper().writeValueAsString(custDetails);
		} catch (Exception ex) {
			log.info("Exception occured in getCustomerDetails: " + ex);
		}
		return customerJsonString;
	}

	@RequestMapping(value = "/brw/requestRoom", method = { RequestMethod.GET ,RequestMethod.POST })
	public String requestRoom(@RequestBody RoomRequest roomRequest) {
		String roomRequestJson="";
		RoomRequest roomReqDetails=null;
		log.info("requestRoom starts...");
		try {
			roomReqDetails =customerService.requestRoom(roomRequest);			
			if(roomReqDetails != null)
				roomRequestJson=new ObjectMapper().writeValueAsString(roomReqDetails);
		} catch (Exception ex) {
			log.info("Exception occured in getCustomerDetails: " + ex);
		}
		return roomRequestJson;
	}
}
