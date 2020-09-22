/**
 * 
 */
package com.postang.model;

import java.util.Date;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class BillPendingRequest {

	private long id;
	private long requestId;
	private String typeOfRequest;
	private double billAmount;
	private Date requestDate;

}
