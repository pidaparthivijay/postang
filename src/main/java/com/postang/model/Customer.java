package com.postang.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class Customer extends User implements Serializable {

	private static final long serialVersionUID = -7211831038006992465L;
	/**
	 * 
	 */

	private long custId;
	private int custAge;
	private Date custDob;

}
