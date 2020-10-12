package com.postang.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class Driver implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_seq")
	@SequenceGenerator(name = "driver_seq", sequenceName = "driver_seq", allocationSize = 1)
	private int driverId;
	@Column(unique = true)
	private String driverLicense;
	private long experience;
	private String driverName;
	private String location;
	@Column(columnDefinition = "varchar(3) default 'N'")
	private String deleted;
	@Transient
	private boolean actionStatus;

}
