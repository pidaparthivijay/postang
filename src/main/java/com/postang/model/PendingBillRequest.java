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
public class PendingBillRequest {

	private long id;
	private long requestId;
	private String typeOfRequest;
	private double billAmount;
	private Date requestDate;

}
