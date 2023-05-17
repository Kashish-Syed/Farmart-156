package com.fmt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Lease extends Equipment {

	private double thirtyDayFee;
	private LocalDate startDate;
	private LocalDate endDate;

	public Lease(String code,String name, String model, double thirtyDayFee,
			LocalDate startDate, LocalDate endDate) {
		super(code, name, model);
		this.thirtyDayFee = thirtyDayFee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Lease(Equipment e, double thirtyDayFee, LocalDate startDate, LocalDate endDate) {
		super(e.getCode(), e.getName(), e.getModel());
		this.thirtyDayFee = thirtyDayFee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public double getThirtyDayFee() {
		return thirtyDayFee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public double getTaxes() {
		double tax = 0.0;
		if (getSubtotal() < 10000) {
			tax = 0;
		} else if ((getSubtotal() > 10000) && (getSubtotal() < 100000)) {
			tax = 500;
		} else {
			tax = 1500;
		}
		return tax;
	}

	public long getDays() {
		return (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1);
	}

	public double getSubtotal() {
		double subtotal = ((double)getDays() / 30.0) * getThirtyDayFee();
		return subtotal;
	}

	public double getTotal() {
		return (getTaxes() + getSubtotal());
	}

	@Override
	public String toString() {
		return String.format("%s\t\t(Lease) %s %s\n\t%d days (%s -> %s) @ $%.2f/ 30 days\n\t  \t\t\t\t\t\t\t\t  $  %.2f\n",
				getCode(), getName(), getModel(), getDays(), getStartDate(), getEndDate(), getThirtyDayFee(),
				getSubtotal());
	}
}
