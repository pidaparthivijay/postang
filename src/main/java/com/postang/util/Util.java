package com.postang.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.postang.constants.Constants;
import com.postang.domain.Amenity;
import com.postang.domain.Customer;
import com.postang.domain.Employee;
import com.postang.domain.Lookup;
import com.postang.domain.OneTimePassword;
import com.postang.domain.RewardPoints;
import com.postang.domain.Room;
import com.postang.domain.RoomRequest;
import com.postang.domain.TourPackage;
import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public class Util implements Constants {

	public int calculateAge(Date custDob) {

		LocalDate today = LocalDate.now();
		LocalDate custDOB = custDob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(custDOB, today);

		return period.getYears();
	}

	public Room constructRoomFromRequest(RoomRequest roomRequest) {
		Room room = new Room();
		room.setRoomModel(roomRequest.getRoomModel());
		room.setRoomType(roomRequest.getRoomType());
		room.setRoomCategory(roomRequest.getRoomCategory());
		return room;
	}

	public double generateBillForRooms(List<Room> totalRoomsList) {
		double totalBill = 0;
		double baseFare = 0;
		for (Room room : totalRoomsList) {
			double roomBill = 0;
			if (SINGLE.equals(room.getRoomCategory())) {
				baseFare = SINGLE_BASE_FARE;
			} else {
				baseFare = DOUBLE_BASE_FARE;
			}
			if (DELUXE.equals(room.getRoomModel())) {
				roomBill = roomBill + (baseFare * DELUXEROOMFARE);
			} else {
				roomBill = roomBill + (baseFare * SUITEROOMFARE);
			}
			if (AC.equals(room.getRoomType())) {
				roomBill = roomBill + (baseFare * ACROOMFARE);
			} else {
				roomBill = roomBill + (baseFare * NONACROOMFARE);
			}
			totalBill = totalBill + roomBill;
		}

		return totalBill;
	}

	public List<Lookup> generateLookupListFromExcelFile(MultipartFile multipartFile) {
		List<Lookup> lookupList = new ArrayList<>();
		Workbook offices;
		try {
			String lowerCaseFileName = multipartFile.getOriginalFilename().toLowerCase();
			if (lowerCaseFileName.endsWith(XLSX_EXTN)) {
				offices = new XSSFWorkbook(multipartFile.getInputStream());

			} else {
				offices = new HSSFWorkbook(multipartFile.getInputStream());
			}
			Sheet lookUpSheet = offices.getSheet(LOOK_UP);
			Iterator<Row> itr = lookUpSheet.iterator();

			while (itr.hasNext()) {
				Row row = itr.next();
				Lookup lookup = new Lookup();
				lookup.setLookupDefName(row.getCell(0).toString());
				lookup.setLookupValue(row.getCell(1).toString());
				lookup.setDisplayName(row.getCell(2).toString());
				lookup.setDeleted(NO);
				lookup.setCreatedDate(new Date());
				lookupList.add(lookup);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lookupList;
	}

	public String generateName(TourPackage tourPackage) {
		String generatedName = "";
		if (tourPackage.getDuration().equals(SHORT)) {
			generatedName += MIN;
		} else if (tourPackage.getDuration().equals(MEDIUM)) {
			generatedName += MED;
		} else if (tourPackage.getDuration().equals(LONG)) {
			generatedName += MAX;
		}
		generatedName = generatedName + UNDERSCORE + tourPackage.getLocation().toUpperCase();
		return generatedName;
	}

	public OneTimePassword generateOTP(User user) {
		OneTimePassword oneTimePassword = new OneTimePassword();
		int randNum = ThreadLocalRandom.current().nextInt(MIN_RAND, MAX_RAND + 1);
		oneTimePassword.setCreatedDate(new Date());
		oneTimePassword.setUserName(user.getUserName());
		oneTimePassword.setValid(true);
		oneTimePassword.setOtpValue(String.valueOf(randNum));
		return oneTimePassword;
	}

	public User generateUserFromCustomer(Customer customer) {
		User genUser = new User();
		try {
			genUser.setName(customer.getCustName());
			genUser.setUserName(customer.getUserName());
			genUser.setPassword(customer.getCustPass());
			genUser.setUserMail(customer.getCustEmail());
			genUser.setUserMob(customer.getCustMob());
			genUser.setUserType(CUSTOMER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return genUser;
	}

	public User generateUserFromEmployee(Employee employee) {
		User genUser = new User();
		try {
			genUser.setName(employee.getEmpName());
			genUser.setUserName(employee.getUserName());
			genUser.setPassword(this.generatePassword());
			genUser.setUserMail(employee.getEmail());
			genUser.setUserType(ADMIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return genUser;
	}

	public Date calculateEndDate(Date startDate, String duration) {
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(startDate);
		endDate.add(Calendar.DATE, 365);
		if (SHORT.equals(duration)) {
			endDate.add(Calendar.DATE, 2);
		} else if (MEDIUM.equals(duration)) {
			endDate.add(Calendar.DATE, 4);
		} else if (LONG.equals(duration)) {
			endDate.add(Calendar.DATE, 6);
		} else if (YEAR.equals(duration)) {
			endDate.add(Calendar.DATE, 365);
		}
		return endDate.getTime();
	}

	public long getPointsForTrxn(String reasonCode) {
		if (ROOM_BOOKING.equals(reasonCode))
			return ROOM_BOOKING_REWARD;
		else if (TOUR_BOOKING.equals(reasonCode))
			return TOUR_BOOKING_REWARD;
		else
			return 50L;
	}

	public String getSHA(String input) {

		try {
			// Static getInstance method is called with hashing SHA
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// digest() method called
			// to calculate message digest of an input
			// and return array of byte
			byte[] messageDigest = md.digest(input.getBytes());
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}
		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public boolean validateLogin(User user, User loginUser) {

		if (user != null && loginUser != null) {
			if (user.getPassword().equals(loginUser.getPassword())) {
				return true;
			}
		}
		return false;
	}

	private String generatePassword() {
		StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (ALPHA_NUMERIC_STRING.length() * Math.random());
			sb.append(ALPHA_NUMERIC_STRING.charAt(index));
		}

		return sb.toString();
	}

	public RewardPoints allocateRewardPoints(User user, String reasonCode) {
		RewardPoints rewardPoints = new RewardPoints();
		rewardPoints.setPointsTransactionName(reasonCode);
		rewardPoints.setPointsEarned(this.getPointsForTrxn(reasonCode));
		rewardPoints.setPointsEarnedDate(new Date());
		rewardPoints.setPointsExpiryDate(this.calculateEndDate(new Date(), YEAR));
		rewardPoints.setUserName(user.getUserName());
		return rewardPoints;
	}

	public double generateBillForAmenities(Amenity amenity, int noOfDays) {
		return amenity.getPrice() * noOfDays;
	}

	public double generateBillForTours(TourPackage tourPackage, long guestCount) {
		return tourPackage.getPricePerHead() * guestCount;
	}

}
