package com.postang.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.postang.constants.Constants;
import com.postang.model.PendingBillRequest;

public class PDFUtil implements Constants {
	Properties mailProperties = new Properties();
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	InputStream stream = loader.getResourceAsStream("mailApp.properties");
	private static final Logger log = LoggerFactory.getLogger(PDFUtil.class);

	public ByteArrayInputStream generatePdf(List<PendingBillRequest> billRequestList, String custName) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, out);
			document = this.writePdf(out, billRequestList, custName);
			document.close();
		} catch (Exception ex) {

			log.error("Exception occurred in generatePdf:", ex);
			ex.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

	public Document writePdf(ByteArrayOutputStream outputStream, List<PendingBillRequest> billRequestList,
			String custName) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, outputStream);
			document.open();			
			mailProperties.load(stream);
			String billPdf = mailProperties.getProperty(BILL_PDF_CONTENT);

			Phrase salutation = new Phrase(HELLO + SPACE + custName + COMMA,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, new BaseColor(123, 3, 252)));
			document.add(salutation);
			Paragraph stuffedData = new Paragraph();
			stuffedData.add(new Chunk(billPdf,
					FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, new BaseColor(0, 189, 242))));
			document.add(stuffedData);
			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(85);
			table.setWidths(new int[] { 2, 2, 4, 3, 3, 3 });

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, new BaseColor(42, 52, 54));
			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase(SNO, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(REQ_ID, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(BILL_CODE, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(TYPE, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(BILL_AMOUNT, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase(REQ_DATE, headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);
			int i = 1;
			Iterator<PendingBillRequest> iterator = billRequestList.iterator();
			while (iterator.hasNext()) {
				PendingBillRequest pendingBill = iterator.next();

				PdfPCell cell;

				cell = new PdfPCell(new Phrase(String.valueOf(i), cellFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(pendingBill.getRequestId()), cellFont));
				cell.setPaddingLeft(5);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(pendingBill.getBillCode(), cellFont));
				cell.setPaddingLeft(5);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(pendingBill.getTypeOfRequest()), cellFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(pendingBill.getBillAmount()), cellFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);

				SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyyy");
				String strDate = EMPTY_STRING;
				if (pendingBill.getRequestDate() != null) {
					strDate = formatter.format(pendingBill.getRequestDate());
				}
				cell = new PdfPCell(new Phrase(String.valueOf(strDate), cellFont));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);
				i++;

			}
			document.add(table);
		} catch (Exception ex) {

			log.error("Exception Occurred in writePdf:", ex);
			ex.printStackTrace();
		}
		document.close();
		return document;
	}
}