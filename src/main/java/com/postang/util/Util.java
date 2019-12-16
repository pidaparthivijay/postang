package com.postang.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import com.postang.model.Constants;
import com.postang.model.OneTimePassword;
import com.postang.model.User;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Util {

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
			log.info("Exception thrown for incorrect algorithm: " + e);
			return null;
		}
	}

	public int calculateAge(Date custDob) {
		log.info("calculateAge starts.. with custDob: " + custDob);

		LocalDate today = LocalDate.now();
		LocalDate custDOB = custDob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(custDOB, today);

		return period.getYears();
	}

	public boolean validateLogin(User user, User loginUser) {

		if (user != null && loginUser != null) {
			if (user.getPassword().equals(loginUser.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public OneTimePassword generateOTP(User user) {
		OneTimePassword oneTimePassword = new OneTimePassword();
		int randNum = ThreadLocalRandom.current().nextInt(Constants.MIN_RAND, Constants.MAX_RAND + 1);
		oneTimePassword.setCreatedDate(new Date());
		oneTimePassword.setUserName(user.getUserName());
		oneTimePassword.setValid(true);
		oneTimePassword.setOtpValue(String.valueOf(randNum));
		return oneTimePassword;
	}

}
