package com.siwiak.java;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@ManagedBean(name = "creditBean", eager = true)
@SessionScoped
public class CreditBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int period;
	private double amount;
	private final double wibor3m = 1.73;

	@ManagedProperty(value = "#{bankierList}")
	private BankierList bankierList;
	private double rateOfInterest;

	public void setBankierList(BankierList bankierList) {
		this.bankierList = bankierList;
	}

	public double getRateOfInterest() {
		if (bankierList != null) {
			rateOfInterest = bankierList.getMarginSelected() + wibor3m;
		}
		return rateOfInterest;
	}

	private ArrayList<Timetable> rates = new ArrayList<Timetable>();

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ArrayList<Timetable> getRates() {

		return rates;
	}

	public void setRates(ArrayList<Timetable> rates) {
		this.rates = rates;
	}

	public String calculate() {
		try {
			if (amount < 1000) {

				FacesMessage msg = new FacesMessage("Wpisz kwotę kredytu o wartości minimalnej 1000", "");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				return null;
			} else {
				System.out.println(getRateOfInterest());
				for (int i = 1; i <= period; i++) {
					rates.add(new Timetable(i, getPmt(), getCapitalPart(i, amount, rateOfInterest),
							getInterest(i, amount, rateOfInterest), getDebtBalance(i, amount, rateOfInterest)));
				}
				return "result.xhtml?faces-redirect=true";
			}
		} catch (Exception e) {

			FacesMessage msg = new FacesMessage("Wprowadzono nieprawidłowe dane! Wprowadz wartości liczbowe.", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return null;
		}
	}

	public String newCalculate() {
		this.amount = 0;
		this.rateOfInterest = 0;
		this.period = 0;
		rates.clear();
		return "home.xhtml?faces-redirect=true";
	}

	public double pmt() {
		return amount * (((rateOfInterest / (100) / 12) * Math.pow((1 + (rateOfInterest / (100) / 12)), period))
				/ (Math.pow((1 + (rateOfInterest / (100) / 12)), period) - 1));
	}

	public double getPmt() {
		return roundDouble2precision(pmt(), 2);
	}

	public double getCapitalPart(int period, double amount, double rateOfInterest) {

		double capitalPart = 0;
		double interest = 0;

		for (int i = 0; i < period; i++) {

			interest = amount * (rateOfInterest / (100) / 12);
			amount = amount - (pmt() - interest);
			capitalPart = pmt() - interest;
		}
		return roundDouble2precision(capitalPart, 2);
	}

	public double getInterest(int period, double amount, double rateOfInterest) {

		double interest = 0;

		for (int i = 0; i < period; i++) {

			interest = amount * (rateOfInterest / (100) / 12);
			amount = amount - (pmt() - interest);

		}
		return roundDouble2precision(interest, 2);
	}

	public double getDebtBalance(int period, double amount, double rateOfInterest) {

		double interest = 0;

		for (int i = 0; i < period; i++) {

			interest = amount * (rateOfInterest / (100) / 12);
			amount = amount - (pmt() - interest);
		}

		return roundDouble2precision(amount, 2);
	}

	public static double roundDouble2precision(double value, int places) {

		if (places < 0)
			throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public void exportToExcel() throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();

		HSSFRow headline = sheet.createRow(0);
		headline.createCell(0).setCellValue("Numer Raty");
		headline.createCell(1).setCellValue("Odsetki");
		headline.createCell(2).setCellValue("Kapitał");
		headline.createCell(3).setCellValue("Rata");
		headline.createCell(4).setCellValue("Saldo zadłużenia");

		for (int i = 0; i < rates.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(rates.get(i).getPeriod());
			row.createCell(1).setCellValue(rates.get(i).getInterest());
			row.createCell(2).setCellValue(rates.get(i).getCapitalPart());
			row.createCell(3).setCellValue(rates.get(i).getPmt());
			row.createCell(4).setCellValue(rates.get(i).getDebtBalance());
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		externalContext.setResponseContentType("application/vnd.ms-excel");
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"TimetableOfcredit.xls\"");

		workbook.write(externalContext.getResponseOutputStream());
		facesContext.responseComplete();
	}

	public void exportToPdf() throws IOException, DocumentException {

		String fileName = "TimetableOfcredit.pdf";
		String contentType = "application/pdf";

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		OutputStream output = ec.getResponseOutputStream();

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, output);
		document.open();

		Paragraph title = new Paragraph("Harmonogram spłaty kredytu");
		title.setAlignment(Element.ALIGN_CENTER);
		PdfPTable table = new PdfPTable(5);

		PdfPCell period = new PdfPCell(new Phrase("Numer Raty"));
		period.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(period);

		PdfPCell interest = new PdfPCell(new Phrase("Odsetki"));
		interest.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(interest);

		PdfPCell capitalPart = new PdfPCell(new Phrase("Kapital"));
		capitalPart.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(capitalPart);

		PdfPCell pmt = new PdfPCell(new Phrase("Rata"));
		pmt.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(pmt);

		PdfPCell debtBalance = new PdfPCell(new Phrase("Saldo"));
		debtBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(debtBalance);
		table.setHeaderRows(1);

		for (int i = 0; i < rates.size(); i++) {

			PdfPCell cell = new PdfPCell(new Phrase(rates.get(i).getPeriod() + ""));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(rates.get(i).getInterest() + ""));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(rates.get(i).getCapitalPart() + ""));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(rates.get(i).getPmt() + ""));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(rates.get(i).getDebtBalance() + ""));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		}

		title.add(table);
		document.add(title);

		document.close();
		fc.responseComplete();
	}
}
