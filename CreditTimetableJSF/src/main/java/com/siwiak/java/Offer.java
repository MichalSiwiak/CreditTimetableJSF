package com.siwiak.java;

import java.io.Serializable;

public class Offer implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String bankName;
	private String productName;
	private double margin;
	private double commission;
	private double rrso;

	public Offer(int id, String bankName, String productName, double margin, double commission, double rrso) {
		this.id = id;
		this.bankName = bankName;
		this.productName = productName;
		this.margin = margin;
		this.commission = commission;
		this.rrso = rrso;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		long temp;
		temp = Double.doubleToLongBits(commission);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(margin);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		temp = Double.doubleToLongBits(rrso);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Offer other = (Offer) obj;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (Double.doubleToLongBits(commission) != Double.doubleToLongBits(other.commission))
			return false;
		if (Double.doubleToLongBits(margin) != Double.doubleToLongBits(other.margin))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (Double.doubleToLongBits(rrso) != Double.doubleToLongBits(other.rrso))
			return false;
		return true;
	}
}

