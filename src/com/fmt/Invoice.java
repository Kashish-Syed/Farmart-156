package com.fmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice implements Comparable<Invoice> {

	private Integer invoiceId;
	private String invoiceCode;
	private String itemCode;
	private Store store;
	private Person customer;
	private Person salesPerson;
	private LocalDate invoiceDate;
	private List<Item> invoiceItems = new ArrayList<>();

	public Invoice(int invoiceId, String invoiceCode, Store store, Person customer, Person salesPerson,
			LocalDate invoiceDate) {
		this.invoiceId = invoiceId;
		this.invoiceCode = invoiceCode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = invoiceDate;
	}

	public Invoice(String invoiceCode, Store store, Person customer, Person salesPerson, LocalDate invoiceDate) {
		this(0, invoiceCode, store, customer, salesPerson, invoiceDate);
	}

	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public List<Item> getInvoiceItems() {
		return invoiceItems;
	}

	public void addItem(Item item) {
		this.invoiceItems.add(item);
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public Store getStore() {
		return store;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public double calculateSubtotal() {
		double subtotal = 0.0;
		for (Item invoiceItem : this.invoiceItems) {
			subtotal += invoiceItem.getSubtotal();
		}
		return Math.round(100 * (subtotal)) / 100.00;
	}

	public double calculateTaxes() {
		double taxes = 0.0;
		for (Item invoiceItem : this.invoiceItems) {
			taxes += invoiceItem.getTaxes();
		}
		return Math.round(100 * (taxes)) / 100.00;
	}

	public double calculateTotal() {
		return Math.round(100 * (calculateTaxes() + calculateSubtotal())) / 100.00;
	}

	public double numItems() {
		int numItems = 0;
		for (Item i : this.invoiceItems) {
			numItems += this.invoiceItems.size();
		}

		return numItems;
	}

	/**
	 * method to print the Summary Report - By Total
	 * 
	 * @return
	 */
	public String getSalesSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-15s %-15s %s,%-15s\t%-10s $%-15.2f $ %.2f", this.invoiceCode,
				this.store.getStoreCode(), this.customer.getLastName(), this.customer.getFirstName(),
				this.invoiceItems.size(), this.calculateTaxes(), this.calculateTotal()));
		return sb.toString();
	}

	/**
	 * Method to print the Invoices report in a formatted manner
	 * 
	 * @return
	 */
	public String invoiceSummaryReport() {

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Invoice\t#%s\nStore\t#%s\nDate\t%s\nCustomer:\n%s\nSales Person:\n%s\n",
				getInvoiceCode(), getStore().getStoreCode(), getInvoiceDate(), getCustomer(), getSalesPerson()));
		sb.append("Item\t\t\t\t\t\t\t\t\t\tTotal\n");
		sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\t\t                           -=-=-=-=-=-\n");
		for (Item i : this.invoiceItems) {
			sb.append(String.format("%s\n\n", i.toString()));
		}
		sb.append("\t\t\t\t\t\t\t\t\t   -=-=-=-=-=-\n");
		sb.append(String.format("\t\t                                                  Subtotal $ %5.2f\n",
				this.calculateSubtotal()));
		sb.append(String.format("\t\t                                                       Tax $ %5.2f\n",
				this.calculateTaxes()));
		sb.append(String.format("\t\t                                               Grand Total $ %5.2f\n",
				this.calculateTotal()));

		return sb.toString();
	}

	@Override
	public int compareTo(Invoice a) {
		int result = (int) (a.calculateTotal() - this.calculateTotal());
		return result;
	}

}
