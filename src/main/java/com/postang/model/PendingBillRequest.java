/**
 * 
 */
package com.postang.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class PendingBillRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3389527743644443690L;
	private long id;
	private long requestId;
	private String billCode;
	private String typeOfRequest;
	private double billAmount;
	private Date requestDate;

}
