package com.aidan.coursework.bookshop.records;

public record AccountAddress (
	String house_no,
	String postcode,
	String city
	
) {
	public String toString() {
		return house_no + " - " + city + " (" + postcode + ")";
	}
}
