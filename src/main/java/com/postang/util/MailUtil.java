package com.postang.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.model.Customer;
import com.postang.model.Employee;
import com.postang.model.User;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
public class MailUtil implements Constants {
	Properties mailProperties = new Properties();
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	InputStream stream = loader.getResourceAsStream("mailApp.properties");
	PDFUtil pdfUtil = new PDFUtil();

	public String sendOTPMail(User user, String oneTimePassword) {
		/*
		 * Properties mailProperties = new Properties(); ClassLoader loader =
		 * Thread.currentThread().getContextClassLoader();
		 */ try {
			/*
			 * InputStream stream = loader.getResourceAsStream("mailApp.properties");
			 */ mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);
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
					if (!StringUtils.isEmpty(user.getUserMail())) {
						InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
						// Setting the recepients from the address variable
						msg.setRecipients(Message.RecipientType.TO, address);
					}
				}
			} else {
				return INVALID_MAIL;
			}
			msg.setSubject("Attempt to Reset Password: " + user.getUserName());
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(OTPMAIL);
			mailText = mailText.replace(MAIL_USERNAME, user.getUserName());
			mailText = mailText.replace(MAIL_OTP, oneTimePassword);
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return MAIL_SUCCESS;
	}

	public String sendSignUpMail(Customer customer) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			String custMail = customer.getCustEmail();
			if (custMail != null) {
				InternetAddress[] address = InternetAddress.parse(custMail, true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return INVALID_MAIL;
			}

			msg.setSubject("Welcome to postang " + customer.getUserName());
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(SIGNUPMAIL);
			mailText = mailText.replace(MAIL_CUSTNAME, customer.getCustName());
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(SINGUP_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return SINGUP_MAIL_SUCCESS;
	}

	public String sendAllocationMail(String email) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			if (email != null) {
				InternetAddress[] address = InternetAddress.parse(email, true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return INVALID_MAIL;
			}

			msg.setSubject(ROOM_ALLOCATED);
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(ALLOCATION_MAIL);
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(ALLOCATION_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return ALLOCATION_MAIL_SUCCESS;
	}

	public String sendCancellationMail(int roomRequestId, User user) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			if (user.getUserMail() != null) {
				InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return INVALID_MAIL;
			}

			msg.setSubject(CANCEL_SUCCESS);
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(CANCEL_SUCCESS_MAIL);
			mailText = mailText.replace(MAIL_CUSTNAME, user.getName());
			mailText = mailText.replace(MAIL_ROOM_REQ_ID, String.valueOf(roomRequestId));
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(CANCEL_SUCCESS_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return CANCEL_SUCCESS_MAIL_SUCCESS;
	}

	public String sendCancellationFailMail(int roomRequestId, User user) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			if (user.getUserMail() != null) {
				InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return INVALID_MAIL;
			}

			msg.setSubject(CANCEL_FAILED);
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(CANCEL_FAIL_MAIL);
			mailText = mailText.replace(MAIL_CUSTNAME, user.getName());
			mailText = mailText.replace(MAIL_ROOM_REQ_ID, String.valueOf(roomRequestId));
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(CANCEL_FAILURE_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return CANCEL_FAILURE_MAIL_SUCCESS;
	}

	public String sendSignUpMailForEmployee(Employee emp) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);

			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			String empMail = emp.getEmail();
			if (empMail != null) {
				InternetAddress[] address = InternetAddress.parse(empMail, true);
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				return INVALID_MAIL;
			}

			msg.setSubject("Welcome to postang " + emp.getUserName());
			msg.setSentDate(new Date());
			String mailText = mailProperties.getProperty(EMP_SIGNUP_MAIL);
			mailText = mailText.replace(MAIL_EMPNAME, emp.getEmpName());
			mailText = mailText.replace(MAIL_PASSWORD, emp.getEmpPass());
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(SINGUP_MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return SINGUP_MAIL_SUCCESS;
	}

	public String sendBillMail(User user, ByteArrayInputStream byteArrayInputStream) {
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);
			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});

			MimeMessage msg = new MimeMessage(session);
			if (user != null) {
				{
					if (!StringUtils.isEmpty(user.getUserMail())) {
						InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
						// Setting the recepients from the address variable
						msg.setRecipients(Message.RecipientType.TO, address);
					}
				}
			} else {
				return INVALID_MAIL;
			}
			msg.setSubject("Bill Generated: " + user.getUserName());
			msg.setSentDate(new Date());
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true);
			mimeMessageHelper.addAttachment("Bill.pdf",
					new ByteArrayResource(IOUtils.toByteArray(byteArrayInputStream)));
			String mailText = mailProperties.getProperty(BILL_MAIL);
			mailText = mailText.replace(MAIL_USERNAME, user.getUserName());
			msg.setText(mailText);
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			log.info(MAIL_SUCCESS);
		} catch (Exception mex) {
			log.info("Unable to send an email " + mex);
			mex.printStackTrace();
		}
		return MAIL_SUCCESS;
	}

}