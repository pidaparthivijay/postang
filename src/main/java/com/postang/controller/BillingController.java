/**
 * 
 */
package com.postang.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.PendingBillRequest;
import com.postang.model.RequestDTO;
import com.postang.service.BillingService;
import com.postang.service.RewardPointsService;
import com.postang.util.MailUtil;
import com.postang.util.PDFUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@Log4j2
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class BillingController implements RequestMappings, Constants {

	@Autowired
	BillingService billingService;

	MailUtil mailUtil = new MailUtil();

	PDFUtil pdfUtil = new PDFUtil();

	@Autowired
	RewardPointsService rewardPointsService;

	/*********************
	 *** Bill Operations***
	 *********************/

	@PostMapping(value = PENDING_BILL_VIEW)
	public RequestDTO getPendingBillRequests(@RequestBody RequestDTO requestDTO) {
		String custEmail = requestDTO.getCustomer().getCustEmail();
		log.info("getPendingBillRequests starts..." + custEmail);
		try {
			List<PendingBillRequest> pendingBillRequests = billingService.getPendingBillRequests(custEmail);
			if (CollectionUtils.isEmpty(pendingBillRequests)) {
				requestDTO.setActionStatus(INVALID_MAIL);
			}
			requestDTO.setPendingBillRequests(pendingBillRequests);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in getPendingBillRequests: " + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = PENDING_BILL_PDF, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> generatePDF(@RequestBody RequestDTO requestDTO) {
		String custEmail = requestDTO.getCustomer().getCustEmail();
		log.info("generatePDF starts..." + custEmail);
		HttpHeaders headers = new HttpHeaders();
		headers.add(CONTENT_DISPOSITION, "attachment;filename=bill.pdf");
		InputStreamResource inputStreamResource = new InputStreamResource(
				new ByteArrayInputStream(INVALID_MAIL.getBytes()));
		try {
			ByteArrayInputStream billPdfStream = billingService.generatedBillPdf(custEmail);
			if (billPdfStream != null) {
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(billPdfStream));
			} else {
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
						.body(inputStreamResource);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in generatePDF: " + e);
			e.printStackTrace();

		}
		inputStreamResource = new InputStreamResource(new ByteArrayInputStream(EXCEPTION_OCCURED.getBytes()));
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(inputStreamResource);
	}

	@PostMapping(value = EMPLOYEE_MAIL_BILL)
	public RequestDTO mailBill(@RequestBody RequestDTO requestDTO) {
		String custEmail = requestDTO.getCustomer().getCustEmail();
		log.info("mailBill starts..." + custEmail);
		try {
			requestDTO.setActionStatus(billingService.triggerMailBill(custEmail));
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception occured in mailBill: " + e);
			e.printStackTrace();
		}
		return requestDTO;
	}
}
