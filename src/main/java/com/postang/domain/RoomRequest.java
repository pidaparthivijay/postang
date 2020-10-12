package com.postang.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Subrahmanya Vijay
 *
 */
@Entity
@Data
public class RoomRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247577259462744510L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomReq_seq")
	@SequenceGenerator(name = "roomReq_seq", sequenceName = "roomReq_seq", allocationSize = 1)
	private int requestId;
	private int userId;
	private String custName, guestName;
	private Date checkInDate;
	@Column(name="requestDate", columnDefinition="DATE DEFAULT CURRENT_DATE")
	private Date requestDate;
	private Date checkOutDate;
	private int roomCount;
	private int guestCount;
	private String guestGen;
	@Transient
	@JsonIgnore
	private List<Room> roomsList= new ArrayList<Room>();
	private String roomModel;
	private String roomType;
	private String roomCategory;
	@Column(columnDefinition="varchar(3) default 'P'")
	private String roomRequestStatus;
	@Column(columnDefinition="int default '0'")
	private int roomNumber;
	/**
	 * PEND => PENDING
	 * GEND => GENERATED
	 * PAID => PAID
	 */
	@Column(columnDefinition="varchar(4) default 'PEND'")
	private String billStatus;
}
