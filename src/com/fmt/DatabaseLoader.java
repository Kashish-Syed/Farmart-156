package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseLoader {

	/**
	 * This method loads the address provided the addressId
	 * 
	 * @param addressId
	 * @return
	 */
	public static Address loadAddressById(Integer addressId) {
		Map<Integer, Address> address = new HashMap<>();
		Address a = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = """
				select a.addressId, a.street, a.city , s.state, a.zip, c.country from Address a
				join State s on s.stateId = a.stateId
				join Country c on c.countryId = a.countryId
				where a.addressId = ?
				""";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting address by their Id...");
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String street = rs.getString("a.street");
				String zipCode = rs.getString("a.zip");
				String city = rs.getString("a.city");
				String state = rs.getString("s.state");
				String country = rs.getString("c.country");
				a = new Address(addressId, street, city, zipCode, state, country);
				address.put(addressId, a);
			} else {
				throw new IllegalStateException("No such address in database with id = " + addressId);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded address by their Id successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return a;
	}

	/**
	 * Loads the person object from the database identified by the given personId
	 * 
	 * @param personId
	 * @return
	 */
	public static Person loadPersonById(int personId) {

		Map<Integer, Person> person = new HashMap<>();

		Person p = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = "select personId, code, firstName, lastName, addressId from Person where personId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting person by their Id...");
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			if (rs.next()) {
				String personCode = rs.getString("code");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address a = loadAddressById(rs.getInt("addressId"));
				p = new Person(personId, personCode, firstName, lastName, a);
				p = loadEmails(p);
				person.put(personId, p);
			} else {
				throw new IllegalStateException("No such person in database with id = " + personId);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded person by their Id successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return p;
	}

	/**
	 * Loading emails of every person by taken in a Person Object
	 * 
	 * @param p
	 */
	private static Person loadEmails(Person p) {
		Connection conn = ConnectionFactory.getConnection();

		String query = "select address from Email where personId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting emails...");
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, p.getPersonId());
			rs = ps.executeQuery();
			while (rs.next()) {
				String address = rs.getString("address");
				p.addEmail(address);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded emails successfully!");

		return p;
	}

	/**
	 * Loading information for a store from the database given the storeId
	 * 
	 * @return
	 */
	public static Store loadStoreById(int storeId) {

		Map<String, Store> stores = new HashMap<>();
		Store s = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = """
					select storeId, s.code, p.code as personCode, p.firstName as firstName, p.lastName as lastName, address as addressId
					from Store s
					join Person p on p.personId = s.managerId
					where s.storeId = ?
				""";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting store by their Id...");
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, storeId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String storeCode = rs.getString("code");
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address a = loadAddressById(rs.getInt("addressId"));
				Person p = new Person(personCode, firstName, lastName);
				s = new Store(storeId, storeCode, p, a);
				stores.put(storeCode, s);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded store by their Id successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return s;

	}

	/**
	 * Loading information for all store from the database
	 * 
	 * @return
	 */
	public static Map<String, Store> loadStores() {

		Map<String, Store> stores = new HashMap<>();
		Store s = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = """
					select storeId, s.code, p.code as personCode, p.firstName as firstName, p.lastName as lastName, address as addressId
					from Store s
					join Person p on p.personId = s.managerId
				""";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting stores...");
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int storeId = rs.getInt("storeId");
				String storeCode = rs.getString("code");
				String personCode = rs.getString("personCode");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				Address a = loadAddressById(rs.getInt("addressId"));
				Person p = new Person(personCode, firstName, lastName);
				s = new Store(storeId, storeCode, p, a);
				stores.put(storeCode, s);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			ConnectionFactory.log.error("Error!", e);

			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded stores successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return stores;

	}

	/**
	 * Loading information for all invoices from the database
	 * 
	 * @return
	 */
	public static Map<String, Invoice> loadInvoices() {
		Map<String, Invoice> invoices = new HashMap<>();
		Invoice i = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = """
					select invoiceId, code, storeId, customerId, salesPersonId, date from Invoice
				""";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting invoices...");
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("code");
				int storeId = rs.getInt("storeId");
				Store store = loadStoreById(storeId);
				int customerId = rs.getInt("customerId");
				int salesPersonId = rs.getInt("salesPersonId");
				Person customer = loadPersonById(customerId);
				Person salesPerson = loadPersonById(salesPersonId);
				LocalDate date = LocalDate.parse(rs.getString("date"));
				i = new Invoice(invoiceId, invoiceCode, store, customer, salesPerson, date);

				List<Item> items = loadItems(invoiceId);
				for (Item it : items) {
					i.addItem(it);
				}

				invoices.put(invoiceCode, i);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded invoices successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return invoices;

	}

	/**
	 * Loading information for all the Items corresponding to the given invoiceId
	 * 
	 * @return
	 */
	public static List<Item> loadItems(int invoiceId) {
		List<Item> items = new ArrayList<>();
		Item i = null;

		Connection conn = ConnectionFactory.getConnection();

		String query = """
				select i.itemId as itemId,
				it.invoiceId as invoiceId,
				i.code as itemCode,
				i.type as type,
				it.equipType as equipType,
				i.name as itemName,
				i.model as model,
				it.price as price,
				it.thirtyDayfee as thirtyDayFee,
				it.startDate as startDate,
				it.endDate as endDate,
				i.unit as unit,
				i.pricePerUnit as pricePerUnit,
				it.quantity as quantity,
				i.hourlyRate as hourlyRate,
				it.numberOfHours as numberOfHours
				from Item i
				join InvoiceItem it on it.itemId = i.itemId
				where it.invoiceId = ?
				""";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ConnectionFactory.log.info("Getting items...");
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoiceId);
			rs = ps.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				String code = rs.getString("itemCode");
				String type = rs.getString("type");
				String equipType = rs.getString("equipType");
				String name = rs.getString("itemName");
				String model = rs.getString("model");
				double price = rs.getDouble("price");
				double thirtyDayFee = rs.getDouble("thirtyDayFee");
				LocalDate startDate = LocalDate.parse(rs.getString("startDate"));
				LocalDate endDate = LocalDate.parse(rs.getString("endDate"));
				String unit = rs.getString("unit");
				double pricePerUnit = rs.getDouble("pricePerUnit");
				double quantity = rs.getDouble("quantity");
				double hourlyRate = rs.getDouble("hourlyRate");
				double numberOfHours = rs.getDouble("numberOfHours");
				if (type.equals("E")) {
					if (equipType.equals("P")) {
						i = new Purchase(code, name, model, price);
					} else if (equipType.equals("L")) {
						i = new Lease(code, name, model, thirtyDayFee, startDate, endDate);
					}
				} else if (type.equals("P")) {
					Product p = new Product(itemId, code, name, unit, pricePerUnit);
					i = new Product(p, quantity);
				} else if (type.equals("S")) {
					Service s = new Service(itemId, code, name, hourlyRate);
					i = new Service(s, numberOfHours);
				}
				items.add(i);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException : ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ConnectionFactory.log.info("Loaded items successfully!");

		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return items;

	}

}
