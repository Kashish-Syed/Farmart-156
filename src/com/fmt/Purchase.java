package com.fmt;

public class Purchase extends Equipment {

	private double price;

	public Purchase(String code, String name, String model, double price) {
		super(code, name, model);
		this.price = price;
	}
	
	public Purchase(Equipment item, double price) {
		super(item.getCode(), item.getName(), item.getModel());
		this.price = price;
	}

	public double getSubtotal() {
		return price;
	}

	public double getTaxes() {
		return 0;
	}

	public double getTotal() {
		return (getSubtotal() + getTaxes());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t\t(Purchase) %s %s\n", getCode(), getName(), getModel()));
		sb.append(String.format("  %75s%5.2f\n","$", this.getSubtotal()));
		return sb.toString();
	}
}
