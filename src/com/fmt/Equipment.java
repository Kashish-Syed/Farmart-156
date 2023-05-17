package com.fmt;

public class Equipment extends Item {

	private String model;

	public Equipment(int itemId, String code,String name, String model) {
		super(itemId, code, name);
		this.model = model;
	}

	public Equipment(String code, String name, String model) {
		this(0, code, name, model);
	}

	public String getModel() {
		return model;
	}

	@Override
	public double getTaxes() {
		return 0;
	}

	@Override
	public double getSubtotal() {
		return 0;
	}

	@Override
	public double getTotal() {
		return 0;
	}

}
