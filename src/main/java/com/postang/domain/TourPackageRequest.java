package com.postang.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;
/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class TourPackageRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tourPackageRequest_seq")
	@SequenceGenerator(name = "tourPackageRequest_seq", sequenceName = "tourPackageRequest_seq", allocationSize = 1)
	private int tourPackageRequestId;
	@NotNull
	private String tourPackageName;
	private long guestCount;
	@Column(name="requestDate", columnDefinition="DATE DEFAULT CURRENT_DATE")
	private Date requestDate;
	private Date startDate;
	private String vehicleName;
	private int userId;
	/**
	 * PEND => PENDING
	 * GEND => GENERATED
	 * PAID => PAID
	 */
	@Column(columnDefinition="varchar(4) default 'PEND'")
	private String billStatus;
	@Transient
	private boolean actionStatus;
	
	}
