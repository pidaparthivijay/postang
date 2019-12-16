package com.postang.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
public class OneTimePassword implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 504936599515799953L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_seq")
	@SequenceGenerator(name = "otp_seq", sequenceName = "otp_seq", allocationSize = 1)
	private long otpId;
	private String otpValue;
	private Date createdDate;
	private String userName;
	private boolean valid;

}
