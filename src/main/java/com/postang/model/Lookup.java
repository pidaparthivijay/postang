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
public class Lookup implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 504936599515799953L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lookup_seq")
	@SequenceGenerator(name = "lookup_seq", sequenceName = "lookup_seq", allocationSize = 1)
	private long lookupId;
	private String lookupDefName;
	private String lookupValue;
	private String displayName;
	private Date createdDate;
	private Date updateDate;
	private String deleted;

}
