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
	private String roomStatus;
	private Date checkInDate;
	private Date checkOutDate;

	}
