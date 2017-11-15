package com.siwiak.java;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.primefaces.event.SelectEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jsoup.Jsoup;

@ManagedBean(name = "bankierList", eager = true)
@SessionScoped
public class BankierList implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String bankName;
	private String productName;
	private double margin;
	private double commission;
	private double rrso;
	
	private Offer selectedOffer;
	public double marginSelected;

	List<Offer> offers = new ArrayList<Offer>();

	public void onRowSelect(SelectEvent event) {
		try {
			FacesMessage msg = new FacesMessage("Wybrano ofertę: " + ((Offer) event.getObject()).getBankName() + " "
					+ ((Offer) event.getObject()).getProductName(), "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
		}
	}

	public void updateMargin() {
		marginSelected = getSelectedOffer().getMargin();
	}

	@PostConstruct
	public void init() {

		try {
			Document dodument = Jsoup.connect("http://www.bankier.pl/kredyty-hipoteczne/porownaj-oferty").timeout(6000)
					.get();
			Elements productNames = dodument.select("table.show10TableRow td.colProduct");
			Elements productValues1 = dodument.select("table.show10TableRow td.colSorter");
			Elements productValues2 = dodument.select("table.show10TableRow td ul");

			List<String> banks = new ArrayList<String>();
			List<String> products = new ArrayList<String>();
			List<Double> rrsos = new ArrayList<Double>();
			List<Double> margins = new ArrayList<Double>();
			List<Double> commissions = new ArrayList<Double>();

			for (int i = 0; i < productNames.size(); i++) {

			}
			for (Element element : productNames) {
				banks.add(element.select("a.bank").text());
				products.add(element.select("a.product").text());
			}
			for (Element element : productValues1) {
				String rrso = element.select("span.textNowrap").text().replace(',', '.').replace("%", "");
				rrsos.add(Double.parseDouble(rrso));
			}
			for (Element element : productValues2) {
				String margin = element.select("li:first-child span").text().replace(',', '.').replace("%", "");
				String commission = element.select("li:nth-child(2) span").text().replace(',', '.').replace("%", "");
				margins.add(Double.parseDouble(margin));
				commissions.add(Double.parseDouble(commission));
			}
			for (int i = 0; i < banks.size(); i++) {
				Offer offer = new Offer(i, banks.get(i), products.get(i), margins.get(i), commissions.get(i),
						rrsos.get(i));
				offers.add(offer);
			}
		} catch (IOException e) {
		}
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getRrso() {
		return rrso;
	}

	public void setRrso(double rrso) {
		this.rrso = rrso;
	}

	public double getMarginSelected() {
		return marginSelected;
	}

	public void setMarginSelected(double marginSelected) {
		this.marginSelected = marginSelected;
	}

	public Offer getSelectedOffer() {
		return selectedOffer;
	}

	public void setSelectedOffer(Offer selectedOffer) {
		this.selectedOffer = selectedOffer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
