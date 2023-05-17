package com.fmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which holds code, type, and name of each Item
 * 
 * @author Kashish Syed
 *
 */
public abstract class Item {

	private Integer itemId;
	private String code;
	private String name;
	private List<Invoice> invoices = new ArrayList<>();

	public Item(int itemId, String code, String name) {
		this.code = code;
		this.name = name;
	}

	public Item(String code, String name) {
		this(0, code, name);
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public abstract double getTaxes();

	public abstract double getSubtotal();

	public abstract double getTotal();

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Item \t\t\t\t\t\t Total\n");
		sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\t\t -=-=-=-=-=-\n");
		return sb.toString();
	}

}
