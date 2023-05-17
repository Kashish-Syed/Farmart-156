package com.fmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			// deleting InvoiceItem records
			String query1 = "delete from InvoiceItem";
			ps = conn.prepareStatement(query1);
			ps.executeUpdate();
			ps.close();

			// deleting Item records
			String query2 = "delete from Item";
			ps = conn.prepareStatement(query2);
			ps.executeUpdate();
			ps.close();

			// deleting Invoice records
			String query3 = "delete from Invoice";
			ps = conn.prepareStatement(query3);
			ps.executeUpdate();
			ps.close();

			// deleting Store records
			String query4 = "delete from Store";
			ps = conn.prepareStatement(query4);
			ps.executeUpdate();
			ps.close();

			// deleting Email records
			String query5 = "delete from Email";
			ps = conn.prepareStatement(query5);
			ps.executeUpdate();
			ps.close();

			// deleting Person records
			String query6 = "delete from Person";
			ps = conn.prepareStatement(query6);
			ps.executeUpdate();
			ps.close();

			// deleting Address records
			String query7 = "delete from Address";
			ps = conn.prepareStatement(query7);
			ps.executeUpdate();
			ps.close();

			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Helper method to add address and returning the addressId
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return
	 */
	private static int addAddress(String street, String city, String state, String zip, String country) {

		int countryId = 0;
		int stateId = 0;
		int addressId = 0;

		Connection conn = ConnectionFactory.getConnection();

		/**
		 * Checking and adding country in the database if does not exists
		 */
		String query = "select countryId from Country where country = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, country);
			rs = ps.executeQuery();
			if (rs.next()) {
				countryId = rs.getInt("countryId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (countryId == 0) {
			String countryQuery = "INSERT INTO Country (country) VALUES (?)";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(countryQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, country);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				countryId = rs.getInt(1);
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		/**
		 * Checking and adding state to the database if does not exists
		 */
		String query1 = " select stateId from State where state = ?";
		ps = null;
		rs = null;
		try {
			ps = conn.prepareStatement(query1);
			ps.setString(1, state);
			rs = ps.executeQuery();
			if (rs.next()) {
				stateId = rs.getInt("stateId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (stateId == 0) {
			String stateQuery = "INSERT INTO State (state) VALUES (?)";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(stateQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, state);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				stateId = rs.getInt(1);
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		/**
		 * Checking and adding address to the database if does not exists
		 */
		String query3 = "select addressId from Address where street = ?";
		ps = null;
		rs = null;
		try {
			ps = conn.prepareStatement(query3);
			ps.setString(1, street);
			rs = ps.executeQuery();
			if (rs.next()) {
				addressId = rs.getInt("addressId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (addressId == 0) {
			String streetQuery = """
					INSERT INTO Address
					(street, city, stateId, zip, countryId)
					VALUES
					(?, ?, ?, ?, ?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(streetQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setInt(3, stateId);
				ps.setString(4, zip);
				ps.setInt(5, countryId);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				addressId = rs.getInt(1);
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

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

		return addressId;
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {

		int personId = 0;

		Connection conn = ConnectionFactory.getConnection();
		int addressId = addAddress(street, city, state, zip, country);
		
		String query = "select personId from Person where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");	
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (personId == 0) {
			String personQuery = """
					INSERT INTO Person
					(code,firstName,lastName,addressId) VALUES
					(?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, personCode);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setInt(4, addressId);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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
	}

	/**
	 * Helper method to get a person by the provided code and returns a personId
	 * 
	 * @param personCode
	 * @return
	 */
	private static int getPersonByCode(String personCode) {
		int personId = 0;

		Connection conn = ConnectionFactory.getConnection();
		String query = "select personId from Person where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				personId = rs.getInt("personId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (personId == 0) {
			throw new IllegalArgumentException();
		}

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

		return personId;
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		int emailId = 0;
		int personId = getPersonByCode(personCode);
		Connection conn = ConnectionFactory.getConnection();

		String query = "select emailId from Email where personId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				emailId = rs.getInt("emailId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (emailId == 0) {
			String emailQuery = """
					INSERT INTO Email
					(address,personId) VALUES
					(?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(emailQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, email);
				ps.setInt(2, personId);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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
	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {

		int storeId = 0;

		Connection conn = ConnectionFactory.getConnection();
		int addressId = addAddress(street, city, state, zip, country);
		int managerId = getPersonByCode(managerCode);

		String query = "select storeId from Store where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (storeId == 0) {
			String personQuery = """
					INSERT INTO Store
					(code, managerId, address) VALUES
					(?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, storeCode);
				ps.setInt(2, managerId);
				ps.setInt(3, addressId);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}

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
	}

	/**
	 * Helper method to get a store by the provided code and returns the storeId
	 * 
	 * @param storeCode
	 * @return
	 */
	private static int getStoreByCode(String storeCode) {
		int storeId = 0;

		Connection conn = ConnectionFactory.getConnection();
		String query = "select storeId from Store where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, storeCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (storeId == 0) {
			throw new IllegalArgumentException();
		}

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

		return storeId;
	}

	/**
	 * Adds a product record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>unit</code> and <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addProduct(String code, String name, String unit, double pricePerUnit) {
		Connection conn = ConnectionFactory.getConnection();
		int itemId = 0;

		String query = "select itemId from Item where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (itemId == 0) {
			String personQuery = """
					INSERT INTO Item
					(code, name, type, unit, pricePerUnit)
					VALUES
					(?,?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "P");
				ps.setString(4, unit);
				ps.setDouble(5, pricePerUnit);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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
	}

	/**
	 * Adds an equipment record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addEquipment(String code, String name, String modelNumber) {
		Connection conn = ConnectionFactory.getConnection();
		int itemId = 0;

		String query = "select itemId from Item where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (itemId == 0) {
			String personQuery = """
					INSERT INTO Item
					(code, name, type, model)
					VALUES
					(?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "E");
				ps.setString(4, modelNumber);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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

	}

	/**
	 * Adds a service record to the database with the given <code>code</code>,
	 * <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */
	public static void addService(String code, String name, double costPerHour) {
		Connection conn = ConnectionFactory.getConnection();
		int itemId = 0;

		String query = "select itemId from Item where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (itemId == 0) {
			String personQuery = """
					INSERT INTO Item
					(code, name, type, hourlyRate)
					VALUES
					(?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, code);
				ps.setString(2, name);
				ps.setString(3, "S");
				ps.setDouble(4, costPerHour);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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

	}

	/**
	 * Helper method to get item by the provided code and returns the itemId
	 * 
	 * @param itemCode
	 * @return
	 */
	private static int getItemByCode(String itemCode) {

		Connection conn = ConnectionFactory.getConnection();
		int itemId = 0;
		String query = "select itemId from Item where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, itemCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				itemId = rs.getInt("itemId");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (itemId == 0) {
			throw new IllegalArgumentException();
		}
		return itemId;
	}

	/**
	 * Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
	 * @param invoiceDate
	 */
	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			String invoiceDate) {
		Connection conn = ConnectionFactory.getConnection();
		int invoiceId = 0;
		int storeId = getStoreByCode(storeCode);
		int customerId = getPersonByCode(customerCode);
		int salesPersonId = getPersonByCode(salesPersonCode);

		String query = "select invoiceId from Invoice where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceId = rs.getInt("invoiceId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (invoiceId == 0) {
			String personQuery = """
					INSERT INTO Invoice
					(code, storeId, customerId, salesPersonId, date)
					VALUES
					(?,?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, invoiceCode);
				ps.setInt(2, storeId);
				ps.setInt(3, customerId);
				ps.setInt(4, salesPersonId);
				ps.setString(5, invoiceDate);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}
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

	}

	/**
	 * Helper method to get the Invoice by the provided code and returns the
	 * invoiceId
	 * 
	 * @param invoiceCode
	 * @return
	 */
	private static int getInvoiceByCode(String invoiceCode) {
		Connection conn = ConnectionFactory.getConnection();
		int invoiceId = 0;
		String query = "select invoiceId from Invoice where code = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceId = rs.getInt("invoiceId");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (invoiceId == 0) {
			throw new IllegalArgumentException();
		}
		return invoiceId;

	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */
	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {

		Connection conn = ConnectionFactory.getConnection();

		int invoiceItemId = 0;
		int invoiceId = getInvoiceByCode(invoiceCode);
		int itemId = getItemByCode(itemCode);

		String query = "select invoiceItemId from InvoiceItem where invoiceId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceItemId = rs.getInt("invoiceItemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (invoiceItemId == 0) {
			String personQuery = """
					INSERT INTO InvoiceItem
					(invoiceId, itemId, quantity)
					VALUES
					(?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setInt(3, quantity);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
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

	}

	/**
	 * Adds a particular equipment <i>purchase</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
		Connection conn = ConnectionFactory.getConnection();

		int invoiceItemId = 0;
		int invoiceId = getInvoiceByCode(invoiceCode);
		int itemId = getItemByCode(itemCode);

		String query = "select invoiceItemId from InvoiceItem where invoiceId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceItemId = rs.getInt("invoiceItemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (invoiceItemId == 0) {
			String personQuery = """
					INSERT INTO InvoiceItem
					(invoiceId, itemId, equipType, price)
					VALUES
					(?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setString(3, "P");
				ps.setDouble(4, purchasePrice);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
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
	}

	/**
	 * Adds a particular equipment <i>lease</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) with the given 30-day <code>periodFee</code> and
	 * <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate,
			String endDate) {
		Connection conn = ConnectionFactory.getConnection();

		int invoiceItemId = 0;
		int invoiceId = getInvoiceByCode(invoiceCode);
		int itemId = getItemByCode(itemCode);

		String query = "select invoiceItemId from InvoiceItem where invoiceId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceItemId = rs.getInt("invoiceItemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (invoiceItemId == 0) {
			String personQuery = """
					INSERT INTO InvoiceItem
					(invoiceId, itemId, equipType, thirtyDayFee, startDate, endDate)
					VALUES
					(?,?,?,?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setString(3, "L");
				ps.setDouble(4, periodFee);
				ps.setString(5, beginDate);
				ps.setString(6, endDate);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
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

	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
		Connection conn = ConnectionFactory.getConnection();

		int invoiceItemId = 0;
		int invoiceId = getInvoiceByCode(invoiceCode);
		int itemId = getItemByCode(itemCode);

		String query = "select invoiceItemId from InvoiceItem where invoiceId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				invoiceItemId = rs.getInt("invoiceItemId");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (invoiceItemId == 0) {
			String personQuery = """
					INSERT INTO InvoiceItem
					(invoiceId, itemId, numberOfHours)
					VALUES
					(?,?,?)
					""";
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, invoiceId);
				ps.setInt(2, itemId);
				ps.setDouble(3, billedHours);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				rs.next();
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
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

	}

}
