package com.postang.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class AmenityRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247577259462744510L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amenityReq_seq")
	@SequenceGenerator(name = "amenityReq_seq", sequenceName = "amenityReq_seq", allocationSize = 1)
	private int amenityRequestId;
	private int userId;
	private long amenityId;
	private Date startDate;
	@Column(name="requestDate", columnDefinition="DATE DEFAULT CURRENT_DATE")
	private Date requestDate;
	@Column(columnDefinition="int default '1'")
	private int noOfDays;
	private String userName;
	/**
	 * PEND => PENDING
	 * GEND => GENERATED
	 * PAID => PAID
	 */
	@Column(columnDefinition="varchar(4) default 'PEND'")
	private String billStatus;
	}
