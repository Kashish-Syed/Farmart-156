package com.fmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class InvoiceReport {

	/**
	 * Method that prints the Summary Report - By Total by using the salesSummary
	 * method in Invoice class
	 * 
	 * @param invoiceMap
	 */
	public static void summaryReport(Map<String, Invoice> invoiceMap) {
		Map<String, Invoice> invoicesMap = DatabaseLoader.loadInvoices();

		List<Invoice> invoicesList = new ArrayList<>(invoicesMap.values());

		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.println(
				"| Summary Report - By Total                                                                      |");
		System.out.println(
				"+------------------------------------------------------------------------------------------------+");

		System.out.println(String.format("%-15s %-15s %-20s %-15s %-15s %s", "Invoice#", "Store", "Customer", "NumItem",
				"Tax", "Total"));

		Collections.sort(invoicesList);
		for (Invoice i : invoicesList) {
			System.out.println(i.getSalesSummary());
		}

		System.out.println(
				"+-------------------------------------------------------------------------------------------------+");

		int numItems = 0;
		for (Invoice i : invoicesMap.values()) {
			numItems += i.getInvoiceItems().size();
		}

		double taxTotal = 0.0;
		for (Invoice i : invoicesMap.values()) {
			taxTotal += i.calculateTaxes();
		}

		double grandTotal = 0.0;
		for (Invoice i : invoicesMap.values()) {
			grandTotal += i.calculateTotal();
		}

		System.out.printf("\t\t\t\t\t\t\t%-10d $%-15.2f $ %-15.2f\n", numItems, taxTotal, grandTotal);
	}

	/**
	 * Method that returns the Store Sales Summary Report using storeSalesSummary
	 * method in the Store class
	 * 
	 * @param storeMap
	 */
	public static void storeSummary(Map<String, Store> storeMap) {

		Map<String, Store> storesMap = DatabaseLoader.loadStores();
		List<Store> storeList = new ArrayList<>(storesMap.values());
		Collections.sort(storeList);

		Map<String, Invoice> invoicesMap = DatabaseLoader.loadInvoices();

		/**
		 * iterating through an invoice map and adding the corresponding invoice to the
		 * corresponding store
		 */
		for (String key : invoicesMap.keySet()) {
			Invoice i = invoicesMap.get(key);
			Store s = storesMap.get(i.getStore().getStoreCode());
			s.addInvoice(i);
		}

		System.out.println("+--------------------------------------------------------------------------------+");
		System.out.println("| Store Slaes Summary Report                                     	         |");
		System.out.println("+--------------------------------------------------------------------------------+");

		System.out.println(String.format("%-15s %-30s %-15s %s", "Store", "Manager", "# Sales", "Grand Total"));

		for (Store s : storeList) {
			System.out.println(s.getStoreSalesSummary());
		}

		System.out.println("+---------------------------------------------------------------------------------+");

		int numItems = 0;
		for (Store i : storesMap.values()) {
			numItems += i.getInvoices().size();
		}

		double grandTotal = 0.0;
		for (Invoice i : invoicesMap.values()) {
			grandTotal += i.calculateTotal();
		}

		System.out.printf("\t\t\t\t\t\t %-15d $%-15.2f\n", numItems, grandTotal);
	}

	/**
	 * Method that prints the Invoice report using the toString method in the
	 * Invoice class
	 * 
	 * @param invoiceMap
	 */
	public static void invoiceSummary(Map<String, Invoice> invoiceMap) {
		for (Invoice i : invoiceMap.values()) {
			System.out.print(i.invoiceSummaryReport());
		}
	}

	/**
	 * Comparator that compares the invoices by last/first name of customer
	 */
	public static Comparator<Invoice> cmpByCustomer = new Comparator<Invoice>() {

		@Override
		public int compare(Invoice a, Invoice b) {
			int result = a.getCustomer().getLastName().compareTo(b.getCustomer().getLastName());
			if (result == 0) {
				result = a.getCustomer().getFirstName().compareTo(b.getCustomer().getFirstName());
			}
			return result;
		}
	};
	

	/**
	 * Comparator that compares the invoices by the total
	 */
	public static Comparator<Invoice> cmpByTotal = new Comparator<Invoice>() {

		@Override
		public int compare(Invoice a, Invoice b) {
			int result = (int) (b.calculateTotal() - a.calculateTotal());
			return result;
		}
	};
	

	/**
	 * Comparator that compares the invoices by the store code and then by sales person's last/first name
	 */
	public static Comparator<Invoice> cmpByStore = new Comparator<Invoice>() {

		@Override
		public int compare(Invoice a, Invoice b) {
			int result = a.getStore().getStoreCode().compareTo(b.getStore().getStoreCode());
			if (result == 0) {
				result = a.getSalesPerson().getLastName().compareTo(b.getSalesPerson().getLastName());
			}
			if (result == 0) {
				result = a.getSalesPerson().getFirstName().compareTo(b.getSalesPerson().getFirstName());
			}
			return result;
		}
	};

	/**
	 * Report which prints out by the last/first name of customer
	 */
	public static void salesByCustomer() {
		Map<String, Invoice> invoicesMap = DatabaseLoader.loadInvoices();
		MyLinkedList<Invoice> invoiceList = new MyLinkedList<Invoice>(cmpByCustomer);

		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.println(
				"| Sales By Customer		                                                                 |");
		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.printf("%-15s %-15s %-25s %-20s %-15s\n", "Sale", "Store", "Customer", "Salesperson", "Total");

		for (Invoice i : invoicesMap.values()) {
			invoiceList.add(i);
		}

		for (int i = 0; i < invoiceList.getSize(); i++) {
			Invoice invoice = invoiceList.getValue(i);
			String invoiceCode = invoice.getInvoiceCode();
			String storeCode = invoice.getStore().getStoreCode();
			String customerName = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			String salesPersonName = invoice.getSalesPerson().getLastName() + ", "
					+ invoice.getSalesPerson().getFirstName();
			double total = invoice.calculateTotal();
			System.out.printf("%-15s %-15s %-25s %-20s $ %5.2f\n", invoiceCode, storeCode, customerName,
					salesPersonName, total);
		}
	}

	/**
	 * Report which prints out by the total value of the invoice from highest to
	 * lowest
	 */
	public static void salesByTotal() {
		Map<String, Invoice> invoicesMap = DatabaseLoader.loadInvoices();
		MyLinkedList<Invoice> invoiceList = new MyLinkedList<Invoice>(cmpByTotal);

		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.println(
				"| Sales By Total	 		                                                         |");
		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.printf("%-15s %-15s %-25s %-20s %-15s\n", "Sale", "Store", "Customer", "Salesperson", "Total");

		for (Invoice i : invoicesMap.values()) {
			invoiceList.add(i);
		}

		for (int i = 0; i < invoiceList.getSize(); i++) {
			Invoice invoice = invoiceList.getValue(i);
			String invoiceCode = invoice.getInvoiceCode();
			String storeCode = invoice.getStore().getStoreCode();
			String customerName = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			String salesPersonName = invoice.getSalesPerson().getLastName() + ", "
					+ invoice.getSalesPerson().getFirstName();
			double total = invoice.calculateTotal();
			System.out.printf("%-15s %-15s %-25s %-20s $ %5.2f\n", invoiceCode, storeCode, customerName,
					salesPersonName, total);
		}
	}

	/**
	 * Report which prints out by the total value of the invoice from highest to
	 * lowest
	 */
	public static void salesByStore() {
		Map<String, Invoice> invoicesMap = DatabaseLoader.loadInvoices();
		MyLinkedList<Invoice> invoiceList = new MyLinkedList<Invoice>(cmpByStore);

		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.println(
				"| Sales By Store   		                                                                 |");
		System.out.println(
				"+------------------------------------------------------------------------------------------------+");
		System.out.printf("%-15s %-15s %-25s %-20s %-15s\n", "Sale", "Store", "Customer", "Salesperson", "Total");

//		System.out.println(invoicesMap);

		for (Invoice i : invoicesMap.values()) {
			invoiceList.add(i);
		}

		for (int i = 0; i < invoiceList.getSize(); i++) {
			Invoice invoice = invoiceList.getValue(i);
			String invoiceCode = invoice.getInvoiceCode();
			String storeCode = invoice.getStore().getStoreCode();
			String customerName = invoice.getCustomer().getLastName() + ", " + invoice.getCustomer().getFirstName();
			String salesPersonName = invoice.getSalesPerson().getLastName() + ", "
					+ invoice.getSalesPerson().getFirstName();
			double total = invoice.calculateTotal();
			System.out.printf("%-15s %-15s %-25s %-20s $ %5.2f\n", invoiceCode, storeCode, customerName,
					salesPersonName, total);
		}
	}

	/**
	 * The main for the project
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		salesByCustomer();
		salesByTotal();
		salesByStore();

	}
}
