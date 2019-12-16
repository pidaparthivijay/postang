package com.postang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

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
	private Date checkOutDate;
	private int roomCount;
	private int guestCount;
	private String guestGen;
	@Transient
	@JsonIgnore
	private List<Room> roomsList= new ArrayList<Room>();
	private String modelOfRooms;
	private String typeOfRooms;
	private String roomRequestStatus;
}
