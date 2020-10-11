/**
 * 
 */
package com.postang.service.common;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.postang.model.PendingBillRequest;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface BillingService {

	double generateBill(String custEmail);

	ByteArrayInputStream generatedBillPdf(String custEmail);

	List<PendingBillRequest> getPendingBillRequests(String custEmail);

	String triggerMailBill(String custEmail);

}
