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
public class Vehicle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_seq")
	@SequenceGenerator(name = "vehicle_seq", sequenceName = "vehicle_seq", allocationSize = 1)
	private int vehicleId;
	@Column(unique = true)
	private String regNum;
	private String vehicleName;
	private String location;
	private Date createdDate;
	private Date updatedDate;
	private String vehicleType;
	@Column(columnDefinition = "varchar(3) default 'N'")
	private String deleted;


}
