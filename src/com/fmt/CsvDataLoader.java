package com.fmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CsvDataLoader {

	private static final String itemsFile = "data/Items.csv";
	private static final String storesFile = "data/Stores.csv";
	private static final String personsFile = "data/Persons.csv";
	private static final String invoiceFile = "data/Invoices.csv";
	private static final String invoiceItemsFile = "data/InvoiceItems.csv";

	/**
	 * Method to load data in Item file
	 * 
	 * @return
	 */
	public static Map<String, Item> loadItemsCsvData() {
		Map<String, Item> allItems = new HashMap<>();
		File f = new File(itemsFile);
		try {
			Scanner s = new Scanner(f);
			int n = Integer.parseInt(s.nextLine());
			for (int i = 0; i < n; i++) {
				Item a = null;
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String code = tokens[0];
				String type = tokens[1];
				String name = tokens[2];
				if (tokens[1].equals("E")) {
					String model = tokens[3];
					a = new Equipment(code, name, model);
				} else if (tokens[1].equals("P")) {
					String unit = tokens[3];
					double unitPrice = Double.parseDouble(tokens[4]);
					a = new Product(code, name, unit, unitPrice);
				} else if (tokens[1].equals("S")) {
					double hourlyRate = Double.parseDouble(tokens[3]);
					a = new Service(code, name, hourlyRate);
				}

				allItems.put(code, a);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return allItems;

	}

	/**
	 * Method to load the Persons file
	 * 
	 * @return
	 */
	public static Map<String, Person> loadPersonsCsvData() {
		Map<String, Person> allPersons = new HashMap<>();
		File f = new File(personsFile);
		try {
			Scanner s = new Scanner(f);
			int n = Integer.parseInt(s.nextLine());
			for (int i = 0; i < n; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String pCode = tokens[0];
				String lastName = tokens[1];
				String firstName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				String country = tokens[7];
				Address personAddress = new Address(street, city, state, zip, country);
				List<String> emails = new ArrayList<String>();
				for (int j = 8; j < tokens.length; j++) {
					String email = new String(tokens[j]);
					emails.add(email);
				}
				Person person = new Person(pCode, firstName, lastName, personAddress);
				allPersons.put(pCode, person);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return allPersons;

	}

	/**
	 * Method that takes in a Map of String and Person to load the Store data
	 * 
	 * @param people
	 * @return
	 */
	public static Map<String, Store> loadStoresCsvData(Map<String, Person> people) {
		Map<String, Store> allStores = new HashMap<>();
		File f = new File(storesFile);
		try {
			Scanner s = new Scanner(f);
			int n = Integer.parseInt(s.nextLine());
			for (int i = 0; i < n; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String sCode = tokens[0];
				Person manager = people.get(tokens[1]);
				String street = tokens[2];
				String city = tokens[3];
				String state = tokens[4];
				String zip = tokens[5];
				String country = tokens[6];
				Address storeAddress = new Address(street, city, state, zip, country);
				Store store = new Store(sCode, manager, storeAddress);
				allStores.put(sCode, store);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return allStores;
	}

	/**
	 * Method that takes in Map of String and Store, and Map of String and Person to load the Invoices data
	 * 
	 * @param storeMap
	 * @param personMap
	 * @return
	 */
	public static Map<String, Invoice> loadInvoicesData(Map<String, Store> storeMap, Map<String, Person> personMap) {

		Map<String, Invoice> allInvoices = new HashMap<>();

		File f = new File(invoiceFile);
		try {
			Scanner s = new Scanner(f);
			int n = Integer.parseInt(s.nextLine());
			for (int i = 0; i < n; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String invoiceCode = tokens[0];
				Store store = storeMap.get(tokens[1]);
				Person customer = personMap.get(tokens[2]);
				Person salesPerson = personMap.get(tokens[3]);
				LocalDate date = LocalDate.parse(tokens[4]);
				Invoice invoice = new Invoice(invoiceCode, store, customer, salesPerson, date);
				allInvoices.put(invoiceCode, invoice);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return allInvoices;

	}

	/**
	 * Void method that takes in two Maps of Item and Invoice to load the InvoicesItems data
	 * @param items
	 * @param invoices
	 */
	public static Map<String, Invoice> loadInvoiceItemData(Map<String, Item> items, Map<String, Invoice> invoices) {
		File f = new File(invoiceItemsFile);
		try {
			Scanner s = new Scanner(f);
			int n = Integer.parseInt(s.nextLine());
			for (int i = 0; i < n; i++) {
				String line = s.nextLine();
				String tokens[] = line.split(",");
				String invoiceCode = tokens[0];
				String itemCode = tokens[1];
				Item item = items.get(itemCode);
				Invoice invoice = invoices.get(invoiceCode);
				if (item instanceof Equipment) {
					if (tokens[2].equals("P")) {
						Double price = Double.parseDouble(tokens[3]);
						invoice.addItem(new Purchase((Equipment) item, price));	
					} else if (tokens[2].equals("L")) {
						Double thirtyDayFee = Double.parseDouble(tokens[3]);
						LocalDate startDate = LocalDate.parse(tokens[4]);
						LocalDate endDate = LocalDate.parse(tokens[5]);
						invoice.addItem(new Lease((Equipment) item, thirtyDayFee, startDate, endDate));	

					}
				} else if (item instanceof Product) {
					Double quantity = Double.parseDouble(tokens[2]);
					invoice.addItem(new Product((Product) item, quantity));
				} else if (item instanceof Service) {
					Double hoursBilled = Double.parseDouble(tokens[2]);
					invoice.addItem(new Service((Service) item, hoursBilled));
				}
				invoices.put(invoiceCode, invoice);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return invoices;

	}

}