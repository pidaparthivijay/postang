package com.postang.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6215474601179625004L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;	
	private String userName;
	private String password;
	private String userType;
	private String custName;
	private String userMob;
	private String userMail;


}
