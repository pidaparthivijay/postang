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


	public double generateBill(String custEmail);

	public ByteArrayInputStream generatedBillPdf(String custEmail);

	public List<PendingBillRequest> getPendingBillRequests(String custEmail);

	public String triggerMailBill(String custEmail);

}
