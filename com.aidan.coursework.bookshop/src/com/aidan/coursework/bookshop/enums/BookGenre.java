package com.aidan.coursework.bookshop.enums;

import com.aidan.coursework.bookshop.database.Serialiser;

public enum BookGenre implements SerialisableEnum {
	Politics,
	Business,
	ComputerScience,
	Biography;
	
	@Override
	public String serialise() {
		return Serialiser.serialise_enum(this);
	}
}
