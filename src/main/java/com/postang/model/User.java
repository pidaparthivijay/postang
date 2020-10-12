package com.postang.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3116345858467176994L;
	/**
	 * 
	 */
	protected String userName;
	protected String password;
	protected String gender;
	protected String userType;
	protected String name;
	protected String mobNumber;
	protected String emailAddress;
	protected String statusMessage;


}
