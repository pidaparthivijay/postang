/**
 * 
 */
package com.postang.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Data
public class EmployeeDTO extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3729868118699610396L;

	private long empId;
	private boolean actionStatus;


}