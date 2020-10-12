package com.postang.domain;

import java.io.Serializable;
import java.util.Date;

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
public class Room implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346601050918065503L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
	@SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 1)
	private int roomNumber;
	private int floorNumber;
	private String roomModel;
	private String roomType;
	private String roomCategory;
	@Column(columnDefinition="varchar(10) default 'vacant'")
	private String roomStatus;
	private Date checkInDate;
	private Date checkOutDate;
	@Transient
	private int countOfRooms;
	@Column(columnDefinition="int default '0'")
	private int roomRequestId;

	}
