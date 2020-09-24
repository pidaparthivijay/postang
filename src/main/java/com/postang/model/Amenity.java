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
/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class Amenity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amenity_seq")
	@SequenceGenerator(name = "amenity_seq", sequenceName = "amenity_seq", allocationSize = 1)
	private int amenityId;
	@Column(unique=true)
	private String amenityName;
	private long price;
	@Column(columnDefinition="varchar(3) default 'N'")
	private String deleted;
	@Transient
	private boolean actionStatus;
	
	}
