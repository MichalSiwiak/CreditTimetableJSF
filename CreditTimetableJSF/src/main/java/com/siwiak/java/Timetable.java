package com.siwiak.java;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "timetable", eager = true)
@RequestScoped
public class Timetable implements Serializable{

	private static final long serialVersionUID = 1L;
	private int period;
	private double pmt;
	private double capitalPart;
	private double interest;
	private double debtBalance;

	public Timetable(int period, double pmt, double capitalPart, double interest, double debtBalance) {
		this.period = period;
		this.pmt = pmt;
		this.capitalPart = capitalPart;
		this.interest = interest;
		this.debtBalance = debtBalance;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getPmt() {
		return pmt;
	}

	public void setPmt(double pmt) {
		this.pmt = pmt;
	}

	public double getCapitalPart() {
		return capitalPart;
	}

	public void setCapitalPart(double capitalPart) {
		this.capitalPart = capitalPart;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getDebtBalance() {
		return debtBalance;
	}

	public void setDebtBalance(double debtBalance) {
		this.debtBalance = debtBalance;
	}

	@Override
	public String toString() {
		return "Timetable [period=" + period + ", pmt=" + pmt + ", capitalPart=" + capitalPart + ", interest="
				+ interest + ", debtBalance=" + debtBalance + "]";
	}
	
	
	
}
