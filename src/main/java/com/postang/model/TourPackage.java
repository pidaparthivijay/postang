package com.postang.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;
@Entity
@Data
public class TourPackage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tourPackage_seq")
	@SequenceGenerator(name = "tourPackage_seq", sequenceName = "tourPackage_seq", allocationSize = 1)
	private int tourPackageId;
	@Column(unique=true)
	private String tourPackageName;
	private String duration;
	private String location;
	private long pricePerHead;
	@Column(columnDefinition="varchar(3) default 'N'")
	private String deleted;
	@Transient
	private boolean actionStatus;
	
	}
