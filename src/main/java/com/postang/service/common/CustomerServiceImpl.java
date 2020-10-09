/**
 * 
 */
package com.postang.service.common;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.model.AmenityRequest;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.PendingBillRequest;
import com.postang.model.Room;
import com.postang.model.RoomRequest;
import com.postang.model.TourPackageRequest;
import com.postang.model.User;
import com.postang.repo.AmenityRequestRepository;
import com.postang.repo.CustomerRepository;
import com.postang.repo.EmployeeRepository;
import com.postang.repo.RoomRepository;
import com.postang.repo.RoomRequestRepository;
import com.postang.repo.TourPackageRequestRepository;
import com.postang.repo.UserRepository;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService,Constants {

	@Autowired
	CustomerRepository custRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	EmployeeRepository empRepo;
	
	@Autowired
	RoomRequestRepository roomReqRepo;

	@Autowired
	RoomRepository roomRepo;
	
	@Autowired
	TourPackageRequestRepository tourPackageRequestRepository;
	
	@Autowired
	AmenityRequestRepository amenityRequestRepository;
	
	Util util = new Util();

	MailUtil mailUtil = new MailUtil();
	
	PDFUtil pdfUtil = new PDFUtil();

	@Override
	public AmenityRequest requestAmenity(AmenityRequest amenityRequest) {
		return amenityRequestRepository.save(amenityRequest);
	}
	
	@Override
	public Customer saveCustomer(Customer customer) {
		User user = new User();
		try {

			Customer validatedCustomer = this.validate(customer);
			if (!StringUtils.isEmpty(validatedCustomer.getStatusMessage())) {
				return validatedCustomer;
			}			
			user = util.generateUserFromCustomer(customer);
			User newUser = userRepo.save(user);
			if (newUser == null) {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setStatusMessage(USER_INVALID);
				return newCus;
			}
			return custRepo.save(customer);
		} catch (Exception ex) {
			log.info("Exception in saveCustomer: " + ex);
			ex.printStackTrace();
		}
		return null;
		
	}

	private Customer validate(Customer customer) {
		List<Customer> customerList = null;
		customerList = custRepo.findByUserName(customer.getUserName());
		if (CollectionUtils.isNotEmpty(customerList)) {
			Customer newCus = new Customer();
			newCus.setCustId(-1L);
			newCus.setActionStatus(false);
			newCus.setStatusMessage(USERNAME_TAKEN);
			return newCus;
		} else {
			int age = util.calculateAge(customer.getCustDob());
			if (age >= 18) {
				customer.setCustAge(age);
			} else {
				Customer newCus = new Customer();
				newCus.setCustId(-1L);
				newCus.setActionStatus(false);
				newCus.setCustAge(-1);
				newCus.setStatusMessage(AGE_INSUFF);
				return newCus;
			}
		}
		return customer;
	}

	
	@Override
	public Customer getCustomerByUserName(String userName) {
		List<Customer> custList= null;
		custList = custRepo.findByUserName(userName);
		if (CollectionUtils.isNotEmpty(custList)) {
			return custList.get(0);
		} else {
			return null;
		}
	}

	
	@Override
	public Optional<Customer> findById(long id) {		
		return custRepo.findById(id);
	}

	@Override
	public Iterable<Customer> findAll() {
		return custRepo.findAll();
	}

	@Override
	public Customer getCustomerDetails(Customer customer) {
		List<Customer> custList= null;
		User user=null; 
		custList = custRepo.findByUserName(customer.getUserName());
		user= userRepo.findByUserName(customer.getUserName());
		if (CollectionUtils.isNotEmpty(custList)) {
			custList.get(0).setCustPass(user.getPassword());
			return custList.get(0);
		} else {
			return null;
		}	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList= null;
		empList = empRepo.findByUserName(userName);
		if (CollectionUtils.isNotEmpty(empList)) {
			return empList.get(0);
		} else {
			return null;
		}	}

	@Override
	public RoomRequest requestRoom(RoomRequest roomRequest) {
		roomRequest.setRoomRequestStatus(PENDING);
		return roomReqRepo.save(roomRequest);
	}

	@Override
	public List<RoomRequest> getMyRequestsList(Customer customer) {		
		return roomReqRepo.findByUserId((int) customer.getUserId());
	}

	@Override
	public String cancelRoomRequest(int roomRequestId) {
		String cancellationStatus="";
		RoomRequest roomRequest=roomReqRepo.findByRequestId(roomRequestId);
		roomRequest.setRoomRequestStatus(CANCEL);
		RoomRequest roomReq=roomReqRepo.save(roomRequest);
		int userId=roomReq.getUserId();
		User user=userRepo.findByUserId(userId);
		if(roomReq.getRequestId()==roomRequestId && roomReq.getRoomRequestStatus().equals(CANCEL)) {
			cancellationStatus=mailUtil.sendCancellationMail(roomRequestId,user);
		}else if(roomReq.getRequestId()==roomRequestId || roomReq.getRoomRequestStatus().equals(CANCEL)) {
			cancellationStatus=mailUtil.sendCancellationFailMail(roomRequestId,user);
		}
		return cancellationStatus;
	}

	@Override
	public double generateBill(String custEmail) {		
		User user= userRepo.findByUserMail(custEmail);
		List<RoomRequest> roomRequestList=roomReqRepo.findByUserId((int) user.getUserId());
		List<RoomRequest> billPendingList = roomRequestList.stream().filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());
		List<Room> totalRoomsList=new ArrayList<>();
		for(RoomRequest roomRequest:billPendingList) {
			List<Room> roomsList=roomRepo.findByRoomRequestId(roomRequest.getRequestId());
			totalRoomsList.addAll(roomsList);
		}
		return util.generateBillForRooms(totalRoomsList);
		
}

	@Override
	public List<PendingBillRequest> getPendingBillRequests(String custEmail) {
		List<PendingBillRequest> pendingBillRequests=new ArrayList<>();
		User user= userRepo.findByUserMail(custEmail);
		if (user != null) {
			List<RoomRequest> roomRequestList = roomReqRepo.findByUserId((int) user.getUserId());
			List<RoomRequest> billPendingList = roomRequestList.stream()
					.filter(p -> BILL_PENDING.equals(p.getBillStatus())).collect(Collectors.toList());
			PendingBillRequest totalBillPendingRequest = new PendingBillRequest();
			if (!CollectionUtils.isEmpty(billPendingList)) {
				pendingBillRequests.addAll(this.convertToBillPendingRequest(billPendingList));
				DoubleSummaryStatistics stats = pendingBillRequests.stream()
						.collect(Collectors.summarizingDouble(PendingBillRequest::getBillAmount));
				totalBillPendingRequest.setBillAmount(stats.getSum());
				totalBillPendingRequest.setTypeOfRequest(TOTAL_BILL_AMOUNT);
			} else {
				totalBillPendingRequest.setBillCode(NO_PENDING_BILLS);
			}
			pendingBillRequests.add(totalBillPendingRequest);
			return pendingBillRequests;
		}
		return new ArrayList<>();

	}

	@Override
	public TourPackageRequest bookTourPackage(TourPackageRequest tourPackageRequest) {
		return tourPackageRequestRepository.save(tourPackageRequest);
	}
	
	@Override
	public Iterable<TourPackageRequest> getAllTourPackageBookings() {
		return tourPackageRequestRepository.findAll();
	}

	public List<PendingBillRequest> convertToBillPendingRequest(List<RoomRequest> billPendingList) {
		List<PendingBillRequest> billPendingRequestList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(billPendingList)) {
			for (RoomRequest roomRequest : billPendingList) {
				PendingBillRequest pendingBillRequest = new PendingBillRequest();
				pendingBillRequest.setBillCode(roomRequest.getGuestName() + UNDERSCORE + roomRequest.getRoomModel()
						+ UNDERSCORE + roomRequest.getRoomCategory() + UNDERSCORE + roomRequest.getRoomType());
				pendingBillRequest.setRequestId(roomRequest.getRequestId());
				List<Room> roomsList = roomRepo.findByRoomRequestId(roomRequest.getRequestId());
				pendingBillRequest.setBillAmount(util.generateBillForRooms(roomsList));
				pendingBillRequest.setTypeOfRequest(ROOM_REQUEST);
				pendingBillRequest.setRequestDate(roomRequest.getRequestDate());
				if (!CollectionUtils.isEmpty(roomsList)) {
					billPendingRequestList.add(pendingBillRequest);
				}
			}
		} else {
			return new ArrayList<>();
		}
		return billPendingRequestList;
	}

	@Override
	public ByteArrayInputStream generatedBillPdf(String custEmail){
		User user = userRepo.findByUserMail(custEmail);
		if (user != null) {
			return pdfUtil.generatePdf(this.getPendingBillRequests(custEmail),
					userRepo.findByUserMail(custEmail).getName());
		} else {
			return null;
		}
	}

	@Override
	public String triggerMailBill(String custEmail) {
		User user = userRepo.findByUserMail(custEmail);
		return mailUtil.sendBillMail(user, this.getPendingBillRequests(custEmail));
	}
}
