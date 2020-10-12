package com.postang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.User;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class MailDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247577259462744510L;
	
	private String templateName;
	private String oneTimePassword;
	private User user;
	private Customer customer;
	private Employee employee;
	private String emailAddress;
	private int roomRequestId;
	private List<PendingBillRequest> pendingBillRequests = new ArrayList<>();
}
