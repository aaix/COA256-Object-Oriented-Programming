package com.aidan.coursework.bookshop.enums;

import com.aidan.coursework.bookshop.database.Serialiser;

public enum BookLanguage implements SerialisableEnum {
	English,
	French;
	
	@Override
	public String serialise() {
		return Serialiser.serialise_enum(this);
	}
}
