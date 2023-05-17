package com.fmt;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private Integer personId;
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;
	private List<String> emails = new ArrayList<>();

	public Person(Integer personId, String personCode, String firstName, String lastName, Address address) {
		this.personId = personId;
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public Person(String personCode, String firstName, String lastName, Address address) {
		this(null, personCode, firstName, lastName, address);
	}

	public Person(String personCode, String firstName, String lastName) {
		this(null, personCode, firstName, lastName, null);
	}

	public int getPersonId() {
		return this.personId;
	}

	public String getPersonCode() {
		return personCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void addEmail(String p) {
		this.emails.add(p);
	}

	@Override
	public String toString() {
		return String.format("%s, %s (%s : %s) \n \t%s\n", getLastName(), getFirstName(), getPersonCode(), getEmails(),
				getAddress());
	}

}
