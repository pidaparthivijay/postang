package com.postang.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.util.StringUtils;

import com.postang.constants.Constants;
import com.postang.domain.User;
import com.postang.model.MailDTO;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
public class MailUtil implements Constants {
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	Properties mailProperties = new Properties();
	PDFUtil pdfUtil = new PDFUtil();
	InputStream stream = loader.getResourceAsStream(MAILAPP_PROPERTIES);

	public String triggerMail(MailDTO mailDTO) {
		String status = EMPTY_STRING;
		User user = mailDTO.getUser();
		String templateName = mailDTO.getTemplateName();
		log.info(user);
		log.info(templateName);
		log.info(mailDTO.getPendingBillRequests());
		try {
			mailProperties.load(stream);
			String mailAddress = mailProperties.getProperty(FRM_ADR);
			String mailPass = mailProperties.getProperty(FRM_PWD);
			// Establishing a session with required user details
			Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAddress, mailPass);
				}
			});
			// Creating a Message object to set the email content
			MimeMessage mimeMessage = new MimeMessage(session);
			// Storing the comma seperated values to email addresses
			/*
			 * Parsing the String with defualt delimiter as a comma by marking the boolean
			 * as true and storing the email addresses in an array of InternetAddress
			 * objects
			 */
			if (user != null) {
				if (!StringUtils.isEmpty(user.getUserMail())) {
					InternetAddress[] address = InternetAddress.parse(user.getUserMail(), true);
					// Setting the recepients from the address variable
					mimeMessage.setRecipients(Message.RecipientType.TO, address);
				}
			} else {
				return INVALID_MAIL;
			}

			mimeMessage.setSentDate(new Date());
			String mailText = EMPTY_STRING;
			if (TEMPLATE_OTP_MAIL.equalsIgnoreCase(templateName)) {
				mimeMessage.setSubject("Attempt to Reset Password: " + user.getUserName());
				mailText = mailProperties.getProperty(OTPMAIL);
				mailText = mailText.replace(MAIL_USERNAME, user.getUserName());
				mailText = mailText.replace(MAIL_OTP, mailDTO.getOneTimePassword());

			} else if (TEMPLATE_CUST_SIGN_UP_MAIL.equalsIgnoreCase(templateName)
					|| TEMPLATE_EMP_SIGN_UP_MAIL.equalsIgnoreCase(templateName)) {

				if (TEMPLATE_CUST_SIGN_UP_MAIL.equalsIgnoreCase(templateName)) {
					mimeMessage.setSubject("Welcome to postang " + mailDTO.getCustomer().getUserName());
					mailText = mailProperties.getProperty(SIGNUPMAIL);
					mailText = mailText.replace(MAIL_CUSTNAME, mailDTO.getCustomer().getCustName());
				} else if (TEMPLATE_EMP_SIGN_UP_MAIL.equalsIgnoreCase(templateName)) {
					mimeMessage.setSubject("Welcome to postang " + mailDTO.getCustomer().getUserName());
					mailText = mailProperties.getProperty(EMP_SIGNUP_MAIL);
					mailText = mailText.replace(MAIL_EMPNAME, mailDTO.getEmployee().getEmpName());
					mailText = mailText.replace(MAIL_PASSWORD, mailDTO.getEmployee().getEmpPass());
				}
			} else if (TEMPLATE_ALLOCATION_MAIL.equalsIgnoreCase(templateName)) {
				mimeMessage.setSubject(ROOM_ALLOCATED);
				mailText = mailProperties.getProperty(ALLOCATION_MAIL);
				mailText = mailText.replace(MAIL_VEHNAME, mailDTO.getVehicle().getVehicleName());
				mailText = mailText.replace(MAIL_DRINAME, mailDTO.getDriver().getDriverName());
				mailText = mailText.replace(MAIL_VEHNUM, mailDTO.getVehicle().getRegNum());
			} else if (TEMPLATE_TOUR_DETAILS.equalsIgnoreCase(templateName)) {
				mimeMessage.setSubject(VEHICLE_DRIVER_DETAILS);
				mailText = mailProperties.getProperty(VEHICLE_DRIVER_DETAILS);
			} else if (TEMPLATE_CANCEL_FAIL_MAIL.equalsIgnoreCase(templateName)
					|| (TEMPLATE_CANCEL_MAIL.equalsIgnoreCase(templateName))) {
				if (TEMPLATE_CANCEL_FAIL_MAIL.equalsIgnoreCase(templateName)) {
					mimeMessage.setSubject(CANCEL_FAILED);
					mailText = mailProperties.getProperty(CANCEL_FAIL_MAIL);

				} else {
					mimeMessage.setSubject(CANCEL_SUCCESS);
					mailText = mailProperties.getProperty(CANCEL_SUCCESS_MAIL);

				}
				mailText = mailText.replace(MAIL_CUSTNAME, user.getName());
				mailText = mailText.replace(MAIL_ROOM_REQ_ID, String.valueOf(mailDTO.getRoomRequestId()));

			} else if (TEMPLATE_BILL_MAIL_CUST.equalsIgnoreCase(templateName)) {
				mimeMessage.setSubject("Bill Generated: " + user.getUserName());
				ByteArrayOutputStream outputStream = null;

				// construct the text body part
				MimeBodyPart textBodyPart = new MimeBodyPart();
				mailText = mailProperties.getProperty(BILL_MAIL);
				mailText = mailText.replace(MAIL_USERNAME, user.getUserName());
				textBodyPart.setText(mailText);

				outputStream = new ByteArrayOutputStream();
				pdfUtil.writePdf(outputStream, mailDTO.getPendingBillRequests(), user.getName());
				byte[] bytes = outputStream.toByteArray();

				// construct the pdf body part
				DataSource dataSource = new ByteArrayDataSource(bytes, APPLICATION_PDF);
				MimeBodyPart pdfBodyPart = new MimeBodyPart();
				pdfBodyPart.setDataHandler(new DataHandler(dataSource));
				pdfBodyPart.setFileName(user.getUserName() + UNDERSCORE + new Date().toString() + PDF_EXTENSION);

				// construct the mime multi part
				MimeMultipart mimeMultipart = new MimeMultipart();
				mimeMultipart.addBodyPart(textBodyPart);
				mimeMultipart.addBodyPart(pdfBodyPart);
				// construct the mime message
				mimeMessage.setContent(mimeMultipart);
			}

			mimeMessage.setText(mailText);
			mimeMessage.setHeader(MAIL_X_PRIORITY, "1");
			Transport.send(mimeMessage);
			status = templateName + MAIL_SUCCESS;
			log.info(status);
		} catch (Exception mex) {
			status = templateName + MAIL_FALIURE;
			log.info("Exception Occurred in triggerMail:" + mex);
			mex.printStackTrace();
		}

		return status;
	}

}