package com.postang.util;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.util.StringUtils;

import com.postang.model.Constants;
import com.postang.model.Customer;
import com.postang.model.User;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MailUtil {
	Properties mailProperties = new Properties();
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	InputStream stream = loader.getResourceAsStream("mailApp.properties");
		
	public String sendOTPMail(User user, String oneTimePassword) {
/*		Properties mailProperties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
*/		try {
/*			InputStream stream = loader.getResourceAsStream("mailApp.properties");
*/			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(Constants.FRM_ADR);
			String mailPass = mailProperties.getProperty(Constants.FRM_PWD);
			// Establishing a session with required user details
			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			// Creating a Message object to set the email content
			MimeMessage msg = new MimeMessage(session);
			// Storing the comma seperated values to email addresses
			// String to = "vijayaditya6894@gmail.com";
			/*
			 * Parsing the String with defualt delimiter as a comma by marking the boolean
			 * as true and storing the email addresses in an array of InternetAddress
			 * objects
			 */
			if (user != null) {
				{
				if(!StringUtils.isEmpty(user.getUserMail()))
				{
					InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
				// Setting the recepients from the address variable
				msg.setRecipients(Message.RecipientType.TO, address);
				}
				}
			} else {
				return Constants.INVALID_MAIL;
			}
			msg.setSubject("Attempt to Reset Password: "+user.getUserName());
			msg.setSentDate(new Date());
			String mailText=mailProperties.getProperty(Constants.OTPMAIL);
			mailText = mailText.replace(Constants.MAIL_USERNAME, user.getUserName());
			mailText = mailText.replace(Constants.MAIL_OTP, oneTimePassword);
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(Constants.MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return Constants.MAIL_SUCCESS;
	}

	public String sendSignUpMail(Customer customer) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(Constants.FRM_ADR);
			String mailPass = mailProperties.getProperty(Constants.FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			String custMail=customer.getCustEmail();
			if (custMail != null) {
				InternetAddress[] address = InternetAddress.parse(custMail, true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return Constants.INVALID_MAIL;
			}
			
			msg.setSubject("Welcome to postang "+customer.getUserName());
			msg.setSentDate(new Date());
			String mailText=mailProperties.getProperty(Constants.SIGNUPMAIL);
			mailText = mailText.replace(Constants.MAIL_CUSTNAME, customer.getCustName());
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(Constants.SINGUP_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return Constants.SINGUP_MAIL_SUCCESS;
	}

}