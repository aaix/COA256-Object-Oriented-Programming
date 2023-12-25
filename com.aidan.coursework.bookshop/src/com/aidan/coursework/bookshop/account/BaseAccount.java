package com.aidan.coursework.bookshop.account;


import com.aidan.coursework.bookshop.database.*;
import com.aidan.coursework.bookshop.enums.AccountType;
import com.aidan.coursework.bookshop.errors.InvalidDatabaseEntry;
import com.aidan.coursework.bookshop.records.AccountAddress;


public class BaseAccount extends DatabaseCompatible {
	int account_id;
	String username;
	String surname;
	AccountAddress address;
	boolean elevated_permissions;
	AccountType type;
	
	public AccountType getType() {
		return type;
	}

	public String getUsername() {
		return username;
	}

	public String getSurname() {
		return surname;
	}

	public AccountAddress getAddress() {
		return address;
	}

	public boolean isElevated_permissions() {
		return elevated_permissions;
	}
	
	public String display() {
		return String.format("%s %s ", username, surname);
	}

	BaseAccount(
		int account_id,
		String username,
		String surname,
		String house_no,
		String postcode,
		String city,
		AccountType type
	) {
		this.account_id = account_id;
		this.username = username;
		this.surname = surname;
		this.address = new AccountAddress(
				house_no,
				postcode,
				city
			);
		this.type = type;
	}
	
	public static BaseAccount deserialise(String line) throws InvalidDatabaseEntry {
		return Deserialiser.deserialise_account(line);
	}
	
	public String serialise() {
		return Serialiser.serialise_account(this);
	}
	
	public int id() {
		return this.account_id;
	}
}
