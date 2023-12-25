package com.aidan.coursework.bookshop.enums;

import com.aidan.coursework.bookshop.database.Serialiser;

public enum AccountType implements SerialisableEnum{
	AdminAccountType,
	UserAccountType;

	@Override
	public String serialise() {
		return Serialiser.serialise_enum(this);
	}
}
