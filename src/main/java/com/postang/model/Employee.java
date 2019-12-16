/**
 * 
 */
package com.postang.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author Vijay
 *
 */
@Entity
@Data
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3729868118699610396L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long empId;
	private String empName;
	private String userName;
	@Transient
	private String empPass;
	@Transient
	private boolean actionStatus;
	@Transient
	private String statusMessage;


}
