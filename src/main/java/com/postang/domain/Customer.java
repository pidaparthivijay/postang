package com.postang.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -450734996184366010L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long custId;
	private String custName;
	private String userName;
	private int custAge;
	private String custGen;
	private Date custDob;
	private String custEmail;
	private String custMob;		
	@Transient
	private String custPass;
	@Transient
	private boolean actionStatus;
	@Transient
	private String statusMessage;
	@Transient
	private long userId;

}
