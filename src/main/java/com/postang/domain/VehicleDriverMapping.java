package com.postang.domain;

import java.io.Serializable;
import java.util.Date;

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
public class VehicleDriverMapping implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 504936599515799953L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vdm_seq")
	@SequenceGenerator(name = "vdm_seq", sequenceName = "vdm_seq", allocationSize = 1)
	private long vdmId;
	private String vehicleRegNum;
	private Date assignedDate;
	private long tourPackageRequestId;
	private String driverLicense;
	private String driverContact;
}
