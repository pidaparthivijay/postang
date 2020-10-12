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
public class RewardPoints implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247577259462744510L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rewardPoint_seq")
	@SequenceGenerator(name = "rewardPoint_seq", sequenceName = "rewardPoint_seq", allocationSize = 1)
	private int id;
	private long userId;
	private long custId;
	private long pointsEarned;	
	private Date pointsEarnedDate;
	private Date pointsExpiryDate;
	@Column(columnDefinition="varchar(3) default 'V'")
	private String pointsStatus;
	private String pointsTransactionName;
	}
