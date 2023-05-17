package com.fmt;

import java.util.ArrayList;
import java.util.List;

public class Store implements Comparable<Store> {

	private Integer storeId;
	private String storeCode;
	private Person manager;
	private Address address;
	private List<Invoice> invoices = new ArrayList<>();

	public Store(String storeCode, Person manager, Address address) {
		this(null, storeCode, manager, address);
	}

	public Store(Integer storeId, String storeCode, Person manager, Address address) {
		this.storeId = storeId;
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
	}

	public Integer getStoreId() {
		return this.storeId;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public Person getManager() {
		return manager;
	}

	public Address getAddress() {
		return address;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void addInvoice(Invoice invoice) {
		this.invoices.add(invoice);
	}

	public double getStoreTotals() {
		double sum = 0;
		for (Invoice i : this.invoices) {
			sum += i.calculateTotal();
		}
		return sum;
	}

	public String getStoreSalesSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15s %s,%-20s\t %-15s $ %.2f", this.storeCode, this.manager.getLastName(),
				this.manager.getFirstName(), this.invoices.size(), this.getStoreTotals()));
		return sb.toString();
	}

	@Override
	public int compareTo(Store o) {
		int result = o.invoices.size() - this.invoices.size();
		if (result == 0) {
			result = this.manager.getLastName().compareTo(o.manager.getLastName());
		}
		if (result == 0) {
			result = this.manager.getFirstName().compareTo(o.manager.getFirstName());
		}
		return result;
	}

}
