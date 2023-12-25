package com.aidan.coursework.bookshop.account;

import com.aidan.coursework.bookshop.enums.AccountType;

public class AdminAccount extends BaseAccount {
	
	public AdminAccount(
		int account_id,
		String username,
		String surname,
		String house_no,
		String postcode,
		String city
	) {

		super(account_id, username, surname, house_no, postcode, city, AccountType.AdminAccountType);
		this.elevated_permissions = true;
	}
	
	public String display() {
		return "[ADMIN] " + super.display();
	}
}
