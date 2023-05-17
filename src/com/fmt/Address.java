package com.fmt;

public class Address {

	private Integer addressId;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;

	public Address(Integer addressId, String street, String city, String state, String zip, String country) {
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public Address(String street, String city, String state, String zip, String country) {
		this(null, street, city, state, zip, country);
	}

	public Integer getAddressId() {
		return this.addressId;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public String toString() {
		return String.format("%s\n\t%s %s %s %s", getStreet(), getCity(), getState(), getZip(), getCountry());
	}
}
