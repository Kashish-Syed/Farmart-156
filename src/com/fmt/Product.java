package com.fmt;

public class Product extends Item {

	private String unit;
	private double pricePerUnit;
	private double quantity;

	public Product(int itemId, String code, String name, String unit, double pricePerUnit) {
		super(itemId, code, name);
		this.unit = unit;
		this.pricePerUnit = pricePerUnit;
	}

	public Product(Product p, double quantity) {
		super(p.getCode(), p.getName());
		this.unit = p.unit;
		this.pricePerUnit = p.pricePerUnit;
		this.quantity = quantity;
	}

	public Product(String code, String name, String unit, double pricePerUnit) {
		this(0, code, name, unit, pricePerUnit);
	}

	public String getUnit() {
		return unit;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public double getQuantity() {
		return quantity;
	}

	@Override
	public double getTaxes() {
		return getSubtotal() * 0.0715;
	}

	@Override
	public double getSubtotal() {
		return (quantity * pricePerUnit);
	}

	@Override
	public double getTotal() {
		return getSubtotal() + getTaxes();
	}

	@Override
	public String toString() {
		return String.format("%s\t\t(Product) %s\n\t%.1f @ $%.1f/%s\n\t\t\t\t\t\t\t\t\t    $%.2f\n", getCode(),
				getName(), getQuantity(), getPricePerUnit(), getUnit(), getSubtotal());
	}
}
