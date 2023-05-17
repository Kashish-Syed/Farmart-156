package com.fmt;

public class Service extends Item {

	private double hourlyRate;
	private double numberOfHours;

	public Service(int itemId, String code, String name, double hourlyRate) {
		super(itemId, code, name);
		this.hourlyRate = hourlyRate;
	}

	public Service(Service s, double numberOfHours) {
		super(s.getCode(), s.getName());
		this.hourlyRate = s.hourlyRate;
		this.numberOfHours = numberOfHours;
	}

	public Service(String code, String name, double hourlyRate) {
		this(0, code, name, hourlyRate);
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public double getNumberOfHours() {
		return numberOfHours;
	}

	@Override
	public double getTaxes() {
		return (getSubtotal() * 0.0345);
	}

	@Override
	public double getSubtotal() {
		return (hourlyRate * numberOfHours);
	}

	@Override
	public double getTotal() {
		return (getSubtotal() + getTaxes());
	}

	@Override
	public String toString() {
		return String.format("%s\t\t(Service)\t %s\n\t%.1f hours @ $%.2f/hr\n\t\t\t\t\t\t\t\t\t    $%.2f\n", getCode(),
				getName(), getNumberOfHours(), getHourlyRate(), getSubtotal());
	}
}
